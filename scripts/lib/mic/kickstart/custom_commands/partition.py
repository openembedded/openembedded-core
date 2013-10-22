# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2013, Intel Corporation.
# All rights reserved.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# DESCRIPTION
# This module provides the OpenEmbedded partition object definitions.
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

import shutil

from pykickstart.commands.partition import *
from mic.utils.oe.misc import *

from mic.kickstart.custom_commands import *

BOOTDD_EXTRA_SPACE = 16384

class Wic_PartData(Mic_PartData):
    removedKeywords = Mic_PartData.removedKeywords
    removedAttrs = Mic_PartData.removedAttrs

    def __init__(self, *args, **kwargs):
        Mic_PartData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()
        self.source = kwargs.get("source", None)
        self.source_file = ""
        self.size = 0

    def _getArgsAsStr(self):
        retval = Mic_PartData._getArgsAsStr(self)

        if self.source:
            retval += " --source=%s" % self.source

        return retval

    def prepare(self, cr_workdir, oe_builddir, boot_type, rootfs_dir,
                bootimg_dir, kernel_dir, native_sysroot):
        """
        Prepare content for individual partitions, depending on
        partition command parameters.
        """
        if not self.source:
            if self.fstype and self.fstype == "swap":
                self.prepare_swap_partition(cr_workdir, oe_builddir,
                                            native_sysroot)
            elif self.fstype:
                self.prepare_empty_partition(cr_workdir, oe_builddir,
                                             native_sysroot)
            return

        if self.source == "bootimg" and boot_type == "pcbios":
            self.prepare_bootimg_pcbios(cr_workdir, oe_builddir, bootimg_dir,
                                        kernel_dir, native_sysroot)
        elif self.source == "bootimg" and boot_type == "efi":
            self.prepare_bootimg_efi(cr_workdir, oe_builddir, bootimg_dir,
                                     kernel_dir, native_sysroot)
        elif self.source.startswith("rootfs"):
            self.prepare_rootfs(cr_workdir, oe_builddir, rootfs_dir,
                                native_sysroot)

    def prepare_bootimg_pcbios(self, cr_workdir, oe_builddir, bootimg_dir,
                               kernel_dir, native_sysroot):
        """
        Prepare content for a legacy bios boot partition.
        """
        staging_kernel_dir = kernel_dir
        staging_data_dir = bootimg_dir

        hdddir = "%s/hdd/boot" % cr_workdir

        install_cmd = "install -m 0644 %s/bzImage %s/vmlinuz" \
            % (staging_kernel_dir, hdddir)
        tmp = exec_cmd(install_cmd)

        install_cmd = "install -m 444 %s/syslinux/ldlinux.sys %s/ldlinux.sys" \
            % (staging_data_dir, hdddir)
        tmp = exec_cmd(install_cmd)

        du_cmd = "du -bks %s" % hdddir
        rc, out = exec_cmd(du_cmd)
        blocks = int(out.split()[0])

        blocks += BOOTDD_EXTRA_SPACE

        # Ensure total sectors is an integral number of sectors per
        # track or mcopy will complain. Sectors are 512 bytes, and we
        # generate images with 32 sectors per track. This calculation is
        # done in blocks, thus the mod by 16 instead of 32.
        blocks += (16 - (blocks % 16))

        # dosfs image, created by mkdosfs
        bootimg = "%s/boot.img" % cr_workdir

        dosfs_cmd = "mkdosfs -n boot -S 512 -C %s %d" % (bootimg, blocks)
        exec_native_cmd(dosfs_cmd, native_sysroot)

        mcopy_cmd = "mcopy -i %s -s %s/* ::/" % (bootimg, hdddir)
        exec_native_cmd(mcopy_cmd, native_sysroot)

        syslinux_cmd = "syslinux %s" % bootimg
        exec_native_cmd(syslinux_cmd, native_sysroot)

        chmod_cmd = "chmod 644 %s" % bootimg
        exec_cmd(chmod_cmd)

        du_cmd = "du -Lbms %s" % bootimg
        rc, out = exec_cmd(du_cmd)
        bootimg_size = out.split()[0]

        self.size = bootimg_size
        self.source_file = bootimg

    def prepare_bootimg_efi(self, cr_workdir, oe_builddir, bootimg_dir,
                            kernel_dir, native_sysroot):
        """
        Prepare content for an EFI (grub) boot partition.
        """
        staging_kernel_dir = kernel_dir
        staging_data_dir = bootimg_dir

        hdddir = "%s/hdd/boot" % cr_workdir

        install_cmd = "install -m 0644 %s/bzImage %s/vmlinuz" % \
            (staging_kernel_dir, hdddir)
        tmp = exec_cmd(install_cmd)

        shutil.copyfile("%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir,
                        "%s/grub.cfg" % cr_workdir)

        cp_cmd = "cp %s/EFI/BOOT/* %s/EFI/BOOT" % (staging_data_dir, hdddir)
        exec_cmd(cp_cmd, True)

        shutil.move("%s/grub.cfg" % cr_workdir,
                    "%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir)

        du_cmd = "du -bks %s" % hdddir
        rc, out = exec_cmd(du_cmd)
        blocks = int(out.split()[0])

        blocks += BOOTDD_EXTRA_SPACE

        # Ensure total sectors is an integral number of sectors per
        # track or mcopy will complain. Sectors are 512 bytes, and we
        # generate images with 32 sectors per track. This calculation is
        # done in blocks, thus the mod by 16 instead of 32.
        blocks += (16 - (blocks % 16))

        # dosfs image, created by mkdosfs
        bootimg = "%s/boot.img" % cr_workdir

        dosfs_cmd = "mkdosfs -n efi -C %s %d" % (bootimg, blocks)
        exec_native_cmd(dosfs_cmd, native_sysroot)

        mcopy_cmd = "mcopy -i %s -s %s/* ::/" % (bootimg, hdddir)
        exec_native_cmd(mcopy_cmd, native_sysroot)

        chmod_cmd = "chmod 644 %s" % bootimg
        exec_cmd(chmod_cmd)

        du_cmd = "du -Lbms %s" % bootimg
        rc, out = exec_cmd(du_cmd)
        bootimg_size = out.split()[0]

        self.size = bootimg_size
        self.source_file = bootimg

    def prepare_rootfs_from_fs_image(self, cr_workdir, oe_builddir,
                                     rootfs_dir):
        """
        Handle an already-created partition e.g. xxx.ext3
        """
        rootfs = oe_builddir
        du_cmd = "du -Lbms %s" % rootfs
        rc, out = exec_cmd(du_cmd)
        rootfs_size = out.split()[0]

        self.size = rootfs_size
        self.source_file = rootfs

    def prepare_rootfs(self, cr_workdir, oe_builddir, rootfs_dir,
                       native_sysroot):
        """
        Prepare content for a rootfs partition i.e. create a partition
        and fill it from a /rootfs dir.

        Currently handles ext2/3/4 and btrfs.
        """
        pseudo = "export PSEUDO_PREFIX=%s/usr;" % native_sysroot
        pseudo += "export PSEUDO_LOCALSTATEDIR=%s/../pseudo;" % rootfs_dir
        pseudo += "export PSEUDO_PASSWD=%s;" % rootfs_dir
        pseudo += "export PSEUDO_NOSYMLINKEXP=1;"
        pseudo += "%s/usr/bin/pseudo " % native_sysroot

        if self.fstype.startswith("ext"):
            return self.prepare_rootfs_ext(cr_workdir, oe_builddir,
                                           rootfs_dir, native_sysroot,
                                           pseudo)
        elif self.fstype.startswith("btrfs"):
            return self.prepare_rootfs_btrfs(cr_workdir, oe_builddir,
                                             rootfs_dir, native_sysroot,
                                             pseudo)

    def prepare_rootfs_ext(self, cr_workdir, oe_builddir, rootfs_dir,
                           native_sysroot, pseudo):
        """
        Prepare content for an ext2/3/4 rootfs partition.
        """
        populate_script = "%s/usr/bin/populate-extfs.sh" % native_sysroot

        image_extra_space = 10240

        image_rootfs = rootfs_dir
        rootfs = "%s/rootfs.%s" % (cr_workdir, self.fstype)

        du_cmd = "du -ks %s" % image_rootfs
        rc, out = exec_cmd(du_cmd)
        actual_rootfs_size = out.split()[0]

        rootfs_size = int(actual_rootfs_size) + image_extra_space

        dd_cmd = "dd if=/dev/zero of=%s bs=1024 seek=%d count=0 bs=1k" % \
            (rootfs, rootfs_size)
        rc, out = exec_cmd(dd_cmd)

        extra_imagecmd = "-i 8192"

        mkfs_cmd = "mkfs.%s -F %s %s" % (self.fstype, extra_imagecmd, rootfs)
        rc, out = exec_native_cmd(mkfs_cmd, native_sysroot)

        populate_cmd = populate_script + " " + image_rootfs + " " + rootfs
        rc, out = exec_native_cmd(pseudo + populate_cmd, native_sysroot)

        # get the rootfs size in the right units for kickstart (Mb)
        du_cmd = "du -Lbms %s" % rootfs
        rc, out = exec_cmd(du_cmd)
        rootfs_size = out.split()[0]

        self.size = rootfs_size
        self.source_file = rootfs

        return 0

    def prepare_rootfs_btrfs(self, cr_workdir, oe_builddir, rootfs_dir,
                             native_sysroot, pseudo):
        """
        Prepare content for a btrfs rootfs partition.

        Currently handles ext2/3/4 and btrfs.
        """
        image_extra_space = 10240

        image_rootfs = rootfs_dir
        rootfs = "%s/rootfs.%s" % (cr_workdir, self.fstype)

        du_cmd = "du -ks %s" % image_rootfs
        rc, out = exec_cmd(du_cmd)
        actual_rootfs_size = out.split()[0]

        rootfs_size = int(actual_rootfs_size) + image_extra_space

        dd_cmd = "dd if=/dev/zero of=%s bs=1024 seek=%d count=0 bs=1k" % \
            (rootfs, rootfs_size)
        rc, out = exec_cmd(dd_cmd)

        mkfs_cmd = "mkfs.%s -b %d -r %s %s" % \
            (self.fstype, rootfs_size * 1024, image_rootfs, rootfs)
        rc, out = exec_native_cmd(pseudo + mkfs_cmd, native_sysroot)

        # get the rootfs size in the right units for kickstart (Mb)
        du_cmd = "du -Lbms %s" % rootfs
        rc, out = exec_cmd(du_cmd)
        rootfs_size = out.split()[0]

        self.size = rootfs_size
        self.source_file = rootfs

    def prepare_empty_partition(self, cr_workdir, oe_builddir, native_sysroot):
        """
        Prepare an empty partition.
        """
        if self.fstype.startswith("ext"):
            return self.prepare_empty_partition_ext(cr_workdir, oe_builddir,
                                                    native_sysroot)
        elif self.fstype.startswith("btrfs"):
            return self.prepare_empty_partition_btrfs(cr_workdir, oe_builddir,
                                                      native_sysroot)

    def prepare_empty_partition_ext(self, cr_workdir, oe_builddir,
                                    native_sysroot):
        """
        Prepare an empty ext2/3/4 partition.
        """
        fs = "%s/fs.%s" % (cr_workdir, self.fstype)

        dd_cmd = "dd if=/dev/zero of=%s bs=1M seek=%d count=0" % \
            (fs, self.size)
        rc, out = exec_cmd(dd_cmd)

        extra_imagecmd = "-i 8192"

        mkfs_cmd = "mkfs.%s -F %s %s" % (self.fstype, extra_imagecmd, fs)
        rc, out = exec_native_cmd(mkfs_cmd, native_sysroot)

        self.source_file = fs

        return 0

    def prepare_empty_partition_btrfs(self, cr_workdir, oe_builddir,
                                      native_sysroot):
        """
        Prepare an empty btrfs partition.
        """
        fs = "%s/fs.%s" % (cr_workdir, self.fstype)

        dd_cmd = "dd if=/dev/zero of=%s bs=1M seek=%d count=0" % \
            (fs, self.size)
        rc, out = exec_cmd(dd_cmd)

        mkfs_cmd = "mkfs.%s -b %d %s" % (self.fstype, self.size * 1024, rootfs)
        rc, out = exec_native_cmd(mkfs_cmd, native_sysroot)

        mkfs_cmd = "mkfs.%s -F %s %s" % (self.fstype, extra_imagecmd, fs)
        rc, out = exec_native_cmd(mkfs_cmd, native_sysroot)

        self.source_file = fs

        return 0

    def prepare_swap_partition(self, cr_workdir, oe_builddir, native_sysroot):
        """
        Prepare a swap partition.
        """
        fs = "%s/fs.%s" % (cr_workdir, self.fstype)

        dd_cmd = "dd if=/dev/zero of=%s bs=1M seek=%d count=0" % \
            (fs, self.size)
        rc, out = exec_cmd(dd_cmd)

        import uuid
        label_str = ""
        if self.label:
            label_str = "-L %s" % self.label
        mkswap_cmd = "mkswap %s -U %s %s" % (label_str, str(uuid.uuid1()), fs)
        rc, out = exec_native_cmd(mkswap_cmd, native_sysroot)

        self.source_file = fs

        return 0

class Wic_Partition(Mic_Partition):
    removedKeywords = Mic_Partition.removedKeywords
    removedAttrs = Mic_Partition.removedAttrs

    def _getParser(self):
        op = Mic_Partition._getParser(self)
        # use specified source file to fill the partition
        # and calculate partition size
        op.add_option("--source", type="string", action="store",
                      dest="source", default=None)
        return op
