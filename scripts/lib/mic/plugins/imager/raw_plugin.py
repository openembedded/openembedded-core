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
import tempfile

from mic import chroot, msger
from mic.utils import misc, fs_related, errors, runner, cmdln
from mic.conf import configmgr
from mic.plugin import pluginmgr
from mic.utils.partitionedfs import PartitionedMount

import mic.imager.raw as raw

from mic.pluginbase import ImagerPlugin
class RawPlugin(ImagerPlugin):
    name = 'raw'

    @classmethod
    @cmdln.option("--compress-disk-image", dest="compress_image", type='choice',
                  choices=("gz", "bz2"), default=None,
                  help="Same with --compress-image")
    @cmdln.option("--compress-image", dest="compress_image", type='choice',
                  choices=("gz", "bz2"), default = None,
                  help="Compress all raw images before package")
    @cmdln.option("--generate-bmap", action="store_true", default = None,
                  help="also generate the block map file")
    @cmdln.option("--fstab-entry", dest="fstab_entry", type='choice',
                  choices=("name", "uuid"), default="uuid",
                  help="Set fstab entry, 'name' means using device names, "
                       "'uuid' means using filesystem uuid")
    def do_create(self, subcmd, opts, *args):
        """${cmd_name}: create raw image

        Usage:
            ${name} ${cmd_name} <ksfile> [OPTS]

        ${cmd_option_list}
        """

        if len(args) != 1:
            raise errors.Usage("Extra arguments given")

        creatoropts = configmgr.create
        ksconf = args[0]

        if creatoropts['runtime'] == "bootstrap":
            configmgr._ksconf = ksconf
            rt_util.bootstrap_mic()

        recording_pkgs = []
        if len(creatoropts['record_pkgs']) > 0:
            recording_pkgs = creatoropts['record_pkgs']

        if creatoropts['release'] is not None:
            if 'name' not in recording_pkgs:
                recording_pkgs.append('name')
            if 'vcs' not in recording_pkgs:
                recording_pkgs.append('vcs')

        configmgr._ksconf = ksconf

        # Called After setting the configmgr._ksconf as the creatoropts['name'] is reset there.
        if creatoropts['release'] is not None:
            creatoropts['outdir'] = "%s/%s/images/%s/" % (creatoropts['outdir'], creatoropts['release'], creatoropts['name'])

        # try to find the pkgmgr
        pkgmgr = None
        backends = pluginmgr.get_plugins('backend')
        if 'auto' == creatoropts['pkgmgr']:
            for key in configmgr.prefer_backends:
                if key in backends:
                    pkgmgr = backends[key]
                    break
        else:
            for key in backends.keys():
                if key == creatoropts['pkgmgr']:
                    pkgmgr = backends[key]
                    break

        if not pkgmgr:
            raise errors.CreatorError("Can't find backend: %s, "
                                      "available choices: %s" %
                                      (creatoropts['pkgmgr'],
                                       ','.join(backends.keys())))

        creator = raw.RawImageCreator(creatoropts, pkgmgr, opts.compress_image,
                                      opts.generate_bmap, opts.fstab_entry)

        if len(recording_pkgs) > 0:
            creator._recording_pkgs = recording_pkgs

        images = ["%s-%s.raw" % (creator.name, disk_name)
                  for disk_name in creator.get_disk_names()]
        self.check_image_exists(creator.destdir,
                                creator.pack_to,
                                images,
                                creatoropts['release'])

        try:
            creator.check_depend_tools()
            creator.mount(None, creatoropts["cachedir"])
            creator.install()
            creator.configure(creatoropts["repomd"])
            creator.copy_kernel()
            creator.unmount()
            creator.generate_bmap()
            creator.package(creatoropts["outdir"])
            if creatoropts['release'] is not None:
                creator.release_output(ksconf, creatoropts['outdir'], creatoropts['release'])
            creator.print_outimage_info()

        except errors.CreatorError:
            raise
        finally:
            creator.cleanup()

        msger.info("Finished.")
        return 0

    @classmethod
    def do_chroot(cls, target, cmd=[]):
        img = target
        imgsize = misc.get_file_size(img) * 1024L * 1024L
        partedcmd = fs_related.find_binary_path("parted")
        disk = fs_related.SparseLoopbackDisk(img, imgsize)
        imgmnt = misc.mkdtemp()
        imgloop = PartitionedMount(imgmnt, skipformat = True)
        imgloop.add_disk('/dev/sdb', disk)
        img_fstype = "ext3"

        msger.info("Partition Table:")
        partnum = []
        for line in runner.outs([partedcmd, "-s", img, "print"]).splitlines():
            # no use strip to keep line output here
            if "Number" in line:
                msger.raw(line)
            if line.strip() and line.strip()[0].isdigit():
                partnum.append(line.strip()[0])
                msger.raw(line)

        rootpart = None
        if len(partnum) > 1:
            rootpart = msger.choice("please choose root partition", partnum)

        # Check the partitions from raw disk.
        # if choose root part, the mark it as mounted
        if rootpart:
            root_mounted = True
        else:
            root_mounted = False
        partition_mounts = 0
        for line in runner.outs([partedcmd,"-s",img,"unit","B","print"]).splitlines():
            line = line.strip()

            # Lines that start with number are the partitions,
            # because parted can be translated we can't refer to any text lines.
            if not line or not line[0].isdigit():
                continue

            # Some vars have extra , as list seperator.
            line = line.replace(",","")

            # Example of parted output lines that are handled:
            # Number  Start        End          Size         Type     File system     Flags
            #  1      512B         3400000511B  3400000000B  primary
            #  2      3400531968B  3656384511B  255852544B   primary  linux-swap(v1)
            #  3      3656384512B  3720347647B  63963136B    primary  fat16           boot, lba

            partition_info = re.split("\s+",line)

            size = partition_info[3].split("B")[0]

            if len(partition_info) < 6 or partition_info[5] in ["boot"]:
                # No filesystem can be found from partition line. Assuming
                # btrfs, because that is the only MeeGo fs that parted does
                # not recognize properly.
                # TODO: Can we make better assumption?
                fstype = "btrfs"
            elif partition_info[5] in ["ext2","ext3","ext4","btrfs"]:
                fstype = partition_info[5]
            elif partition_info[5] in ["fat16","fat32"]:
                fstype = "vfat"
            elif "swap" in partition_info[5]:
                fstype = "swap"
            else:
                raise errors.CreatorError("Could not recognize partition fs type '%s'." % partition_info[5])

            if rootpart and rootpart == line[0]:
                mountpoint = '/'
            elif not root_mounted and fstype in ["ext2","ext3","ext4","btrfs"]:
                # TODO: Check that this is actually the valid root partition from /etc/fstab
                mountpoint = "/"
                root_mounted = True
            elif fstype == "swap":
                mountpoint = "swap"
            else:
                # TODO: Assing better mount points for the rest of the partitions.
                partition_mounts += 1
                mountpoint = "/media/partition_%d" % partition_mounts

            if "boot" in partition_info:
                boot = True
            else:
                boot = False

            msger.verbose("Size: %s Bytes, fstype: %s, mountpoint: %s, boot: %s" % (size, fstype, mountpoint, boot))
            # TODO: add_partition should take bytes as size parameter.
            imgloop.add_partition((int)(size)/1024/1024, "/dev/sdb", mountpoint, fstype = fstype, boot = boot)

        try:
            imgloop.mount()

        except errors.MountError:
            imgloop.cleanup()
            raise

        try:
            if len(cmd) != 0:
                cmdline = ' '.join(cmd)
            else:
                cmdline = "/bin/bash"
            envcmd = fs_related.find_binary_inchroot("env", imgmnt)
            if envcmd:
                cmdline = "%s HOME=/root %s" % (envcmd, cmdline)
            chroot.chroot(imgmnt, None, cmdline)
        except:
            raise errors.CreatorError("Failed to chroot to %s." %img)
        finally:
            chroot.cleanup_after_chroot("img", imgloop, None, imgmnt)

    @classmethod
    def do_unpack(cls, srcimg):
        srcimgsize = (misc.get_file_size(srcimg)) * 1024L * 1024L
        srcmnt = misc.mkdtemp("srcmnt")
        disk = fs_related.SparseLoopbackDisk(srcimg, srcimgsize)
        srcloop = PartitionedMount(srcmnt, skipformat = True)

        srcloop.add_disk('/dev/sdb', disk)
        srcloop.add_partition(srcimgsize/1024/1024, "/dev/sdb", "/", "ext3", boot=False)
        try:
            srcloop.mount()

        except errors.MountError:
            srcloop.cleanup()
            raise

        image = os.path.join(tempfile.mkdtemp(dir = "/var/tmp", prefix = "tmp"), "target.img")
        args = ['dd', "if=%s" % srcloop.partitions[0]['device'], "of=%s" % image]

        msger.info("`dd` image ...")
        rc = runner.show(args)
        srcloop.cleanup()
        shutil.rmtree(os.path.dirname(srcmnt), ignore_errors = True)

        if rc != 0:
            raise errors.CreatorError("Failed to dd")
        else:
            return image
