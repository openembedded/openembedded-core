#!/usr/bin/python -tt
#
# Copyright (c) 2011 Intel, Inc.
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation; version 2 of the License
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc., 59
# Temple Place - Suite 330, Boston, MA 02111-1307, USA.

import os
import shutil
import re

from mic import msger
from mic.utils import misc, fs_related, runner
from mic.utils.errors import CreatorError, MountError
from mic.utils.partitionedfs import PartitionedMount
from mic.imager.livecd import LiveCDImageCreator


class LiveUSBImageCreator(LiveCDImageCreator):
    def __init__(self, *args):
        LiveCDImageCreator.__init__(self, *args)

        self._dep_checks.extend(["kpartx", "parted"])

        # remove dependency of genisoimage in parent class
        if "genisoimage" in self._dep_checks:
            self._dep_checks.remove("genisoimage")

    def _create_usbimg(self, isodir):
        overlaysizemb = 64 #default
        #skipcompress = self.skip_compression?
        fstype = "vfat"
        homesizemb=0
        swapsizemb=0
        homefile="home.img"
        plussize=128
        kernelargs=None

        if fstype == 'vfat':
            if overlaysizemb > 2047:
                raise CreatorError("Can't have an overlay of 2048MB or "
                                   "greater on VFAT")

            if homesizemb > 2047:
                raise CreatorError("Can't have an home overlay of 2048MB or "
                                   "greater on VFAT")

            if swapsizemb > 2047:
                raise CreatorError("Can't have an swap overlay of 2048MB or "
                                   "greater on VFAT")

        livesize = misc.get_file_size(isodir + "/LiveOS")

        usbimgsize = (overlaysizemb + \
                      homesizemb + \
                      swapsizemb + \
                      livesize + \
                      plussize) * 1024L * 1024L

        disk = fs_related.SparseLoopbackDisk("%s/%s.usbimg" \
                                                 % (self._outdir, self.name),
                                             usbimgsize)
        usbmnt = self._mkdtemp("usb-mnt")
        usbloop = PartitionedMount(usbmnt)
        usbloop.add_disk('/dev/sdb', disk)

        usbloop.add_partition(usbimgsize/1024/1024,
                              "/dev/sdb",
                              "/",
                              fstype,
                              boot=True)

        usbloop.mount()

        try:
            fs_related.makedirs(usbmnt + "/LiveOS")

            if os.path.exists(isodir + "/LiveOS/squashfs.img"):
                shutil.copyfile(isodir + "/LiveOS/squashfs.img",
                                usbmnt + "/LiveOS/squashfs.img")
            else:
                fs_related.mksquashfs(os.path.dirname(self._image),
                                      usbmnt + "/LiveOS/squashfs.img")

            if os.path.exists(isodir + "/LiveOS/osmin.img"):
                shutil.copyfile(isodir + "/LiveOS/osmin.img",
                                usbmnt + "/LiveOS/osmin.img")

            if fstype == "vfat" or fstype == "msdos":
                uuid = usbloop.partitions[0]['mount'].uuid
                label = usbloop.partitions[0]['mount'].fslabel
                usblabel = "UUID=%s-%s" % (uuid[0:4], uuid[4:8])
                overlaysuffix = "-%s-%s-%s" % (label, uuid[0:4], uuid[4:8])
            else:
                diskmount = usbloop.partitions[0]['mount']
                usblabel = "UUID=%s" % diskmount.uuid
                overlaysuffix = "-%s-%s" % (diskmount.fslabel, diskmount.uuid)

            args = ['cp', "-Rf", isodir + "/isolinux", usbmnt + "/syslinux"]
            rc = runner.show(args)
            if rc:
                raise CreatorError("Can't copy isolinux directory %s" \
                                   % (isodir + "/isolinux/*"))

            if os.path.isfile("/usr/share/syslinux/isolinux.bin"):
                syslinux_path = "/usr/share/syslinux"
            elif  os.path.isfile("/usr/lib/syslinux/isolinux.bin"):
                syslinux_path = "/usr/lib/syslinux"
            else:
                raise CreatorError("syslinux not installed : "
                                   "cannot find syslinux installation path")

            for f in ("isolinux.bin", "vesamenu.c32"):
                path = os.path.join(syslinux_path, f)
                if os.path.isfile(path):
                    args = ['cp', path, usbmnt + "/syslinux/"]
                    rc = runner.show(args)
                    if rc:
                        raise CreatorError("Can't copy syslinux file " + path)
                else:
                    raise CreatorError("syslinux not installed: "
                                       "syslinux file %s not found" % path)

            fd = open(isodir + "/isolinux/isolinux.cfg", "r")
            text = fd.read()
            fd.close()
            pattern = re.compile('CDLABEL=[^ ]*')
            text = pattern.sub(usblabel, text)
            pattern = re.compile('rootfstype=[^ ]*')
            text = pattern.sub("rootfstype=" + fstype, text)
            if kernelargs:
                text = text.replace("rd.live.image", "rd.live.image " + kernelargs)

            if overlaysizemb > 0:
                msger.info("Initializing persistent overlay file")
                overfile = "overlay" + overlaysuffix
                if fstype == "vfat":
                    args = ['dd',
                            "if=/dev/zero",
                            "of=" + usbmnt + "/LiveOS/" + overfile,
                            "count=%d" % overlaysizemb,
                            "bs=1M"]
                else:
                    args = ['dd',
                            "if=/dev/null",
                            "of=" + usbmnt + "/LiveOS/" + overfile,
                            "count=1",
                            "bs=1M",
                            "seek=%d" % overlaysizemb]
                rc = runner.show(args)
                if rc:
                    raise CreatorError("Can't create overlay file")
                text = text.replace("rd.live.image", "rd.live.image rd.live.overlay=" + usblabel)
                text = text.replace(" ro ", " rw ")

            if swapsizemb > 0:
                msger.info("Initializing swap file")
                swapfile = usbmnt + "/LiveOS/" + "swap.img"
                args = ['dd',
                        "if=/dev/zero",
                        "of=" + swapfile,
                        "count=%d" % swapsizemb,
                        "bs=1M"]
                rc = runner.show(args)
                if rc:
                    raise CreatorError("Can't create swap file")
                args = ["mkswap", "-f", swapfile]
                rc = runner.show(args)
                if rc:
                    raise CreatorError("Can't mkswap on swap file")

            if homesizemb > 0:
                msger.info("Initializing persistent /home")
                homefile = usbmnt + "/LiveOS/" + homefile
                if fstype == "vfat":
                    args = ['dd',
                            "if=/dev/zero",
                            "of=" + homefile,
                            "count=%d" % homesizemb,
                            "bs=1M"]
                else:
                    args = ['dd',
                            "if=/dev/null",
                            "of=" + homefile,
                            "count=1",
                            "bs=1M",
                            "seek=%d" % homesizemb]
                rc = runner.show(args)
                if rc:
                    raise CreatorError("Can't create home file")

                mkfscmd = fs_related.find_binary_path("/sbin/mkfs." + fstype)
                if fstype == "ext2" or fstype == "ext3":
                    args = [mkfscmd, "-F", "-j", homefile]
                else:
                    args = [mkfscmd, homefile]
                rc = runner.show(args)
                if rc:
                    raise CreatorError("Can't mke2fs home file")
                if fstype == "ext2" or fstype == "ext3":
                    tune2fs = fs_related.find_binary_path("tune2fs")
                    args = [tune2fs,
                            "-c0",
                            "-i0",
                            "-ouser_xattr,acl",
                            homefile]
                    rc = runner.show(args)
                    if rc:
                        raise CreatorError("Can't tune2fs home file")

            if fstype == "vfat" or fstype == "msdos":
                syslinuxcmd = fs_related.find_binary_path("syslinux")
                syslinuxcfg = usbmnt + "/syslinux/syslinux.cfg"
                args = [syslinuxcmd,
                        "-d",
                        "syslinux",
                        usbloop.partitions[0]["device"]]

            elif fstype == "ext2" or fstype == "ext3":
                extlinuxcmd = fs_related.find_binary_path("extlinux")
                syslinuxcfg = usbmnt + "/syslinux/extlinux.conf"
                args = [extlinuxcmd,
                        "-i",
                        usbmnt + "/syslinux"]

            else:
                raise CreatorError("Invalid file system type: %s" % (fstype))

            os.unlink(usbmnt + "/syslinux/isolinux.cfg")
            fd = open(syslinuxcfg, "w")
            fd.write(text)
            fd.close()
            rc = runner.show(args)
            if rc:
                raise CreatorError("Can't install boot loader.")

        finally:
            usbloop.unmount()
            usbloop.cleanup()

        # Need to do this after image is unmounted and device mapper is closed
        msger.info("set MBR")
        mbrfile = "/usr/lib/syslinux/mbr.bin"
        if not os.path.exists(mbrfile):
            mbrfile = "/usr/share/syslinux/mbr.bin"
            if not os.path.exists(mbrfile):
                raise CreatorError("mbr.bin file didn't exist.")
        mbrsize = os.path.getsize(mbrfile)
        outimg = "%s/%s.usbimg" % (self._outdir, self.name)

        args = ['dd',
                "if=" + mbrfile,
                "of=" + outimg,
                "seek=0",
                "conv=notrunc",
                "bs=1",
                "count=%d" % (mbrsize)]
        rc = runner.show(args)
        if rc:
            raise CreatorError("Can't set MBR.")

    def _stage_final_image(self):
        try:
            isodir = self._get_isodir()
            fs_related.makedirs(isodir + "/LiveOS")

            minimal_size = self._resparse()

            if not self.skip_minimize:
                fs_related.create_image_minimizer(isodir + "/LiveOS/osmin.img",
                                                  self._image,
                                                  minimal_size)

            if self.skip_compression:
                shutil.move(self._image,
                            isodir + "/LiveOS/ext3fs.img")
            else:
                fs_related.makedirs(os.path.join(
                                        os.path.dirname(self._image),
                                        "LiveOS"))
                shutil.move(self._image,
                            os.path.join(os.path.dirname(self._image),
                                         "LiveOS", "ext3fs.img"))
                fs_related.mksquashfs(os.path.dirname(self._image),
                           isodir + "/LiveOS/squashfs.img")

                self._create_usbimg(isodir)

                if self.pack_to:
                    usbimg = os.path.join(self._outdir, self.name + ".usbimg")
                    packimg = os.path.join(self._outdir, self.pack_to)
                    misc.packing(packimg, usbimg)
                    os.unlink(usbimg)

        finally:
            shutil.rmtree(isodir, ignore_errors = True)
            self._set_isodir(None)

