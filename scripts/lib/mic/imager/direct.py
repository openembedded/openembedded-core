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
# This implements the 'direct' image creator class for 'wic', based
# loosely on the raw image creator from 'mic'
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

import os
import stat
import shutil

from mic import kickstart, msger
from mic.utils import fs_related, runner, misc
from mic.utils.partitionedfs import PartitionedMount
from mic.utils.errors import CreatorError, MountError
from mic.imager.baseimager import BaseImageCreator
from mic.utils.oe.misc import *

class DirectImageCreator(BaseImageCreator):
    """
    Installs a system into a file containing a partitioned disk image.

    DirectImageCreator is an advanced ImageCreator subclass; an image
    file is formatted with a partition table, each partition created
    from a rootfs or other OpenEmbedded build artifact and dd'ed into
    the virtual disk. The disk image can subsequently be dd'ed onto
    media and used on actual hardware.
    """

    def __init__(self, oe_builddir, image_output_dir, rootfs_dir, bootimg_dir,
                 kernel_dir, native_sysroot, hdddir, staging_data_dir,
                 creatoropts=None, pkgmgr=None, compress_image=None,
                 generate_bmap=None, fstab_entry="uuid"):
        """
        Initialize a DirectImageCreator instance.

        This method takes the same arguments as ImageCreator.__init__()
        """
        BaseImageCreator.__init__(self, creatoropts, pkgmgr)

        self.__instimage = None
        self.__imgdir = None
        self.__disks = {}
        self.__disk_format = "direct"
        self._disk_names = []
        self._ptable_format = self.ks.handler.bootloader.ptable
        self.use_uuid = fstab_entry == "uuid"
        self.compress_image = compress_image
        self.bmap_needed = generate_bmap

        self.oe_builddir = oe_builddir
        if image_output_dir:
            self.tmpdir = image_output_dir
            self.cachedir = "%s/cache" % image_output_dir
        self.rootfs_dir = rootfs_dir
        self.bootimg_dir = bootimg_dir
        self.kernel_dir = kernel_dir
        self.native_sysroot = native_sysroot
        self.hdddir = hdddir
        self.staging_data_dir = staging_data_dir
        self.boot_type = ""

    def __write_fstab(self):
        """overriden to generate fstab (temporarily) in rootfs. This
        is called from mount_instroot, make sure it doesn't get called
        from BaseImage.mount()"""

        image_rootfs = self.rootfs_dir

        parts = self._get_parts()

        fstab = image_rootfs + "/etc/fstab"

        self._save_fstab(fstab)
        fstab_lines = self._get_fstab(fstab, parts)
        self._update_fstab(fstab_lines, parts)
        self._write_fstab(fstab, fstab_lines)

        return fstab

    def _update_fstab(self, fstab_lines, parts):
        """Assume partition order same as in wks"""
        for num, p in enumerate(parts, 1):
            if p.mountpoint == "/" or p.mountpoint == "/boot":
                continue
            if self._ptable_format == 'msdos' and num > 3:
                device_name = "/dev/" + p.disk + str(num + 1)
            else:
                device_name = "/dev/" + p.disk + str(num)
            fstab_entry = device_name + "\t" + p.mountpoint + "\t" + p.fstype + "\tdefaults\t0\t0\n"
            fstab_lines.append(fstab_entry)

    def _write_fstab(self, fstab, fstab_lines):
        fstab = open(fstab, "w")
        for line in fstab_lines:
            fstab.write(line)
        fstab.close()

    def _save_fstab(self, fstab):
        """Save the current fstab in rootfs"""
        shutil.copyfile(fstab, fstab + ".orig")

    def _restore_fstab(self, fstab):
        """Restore the saved fstab in rootfs"""
        shutil.move(fstab + ".orig", fstab)

    def _get_fstab(self, fstab, parts):
        """Return the desired contents of /etc/fstab."""
        f = open(fstab, "r")
        fstab_contents = f.readlines()
        f.close()

        return fstab_contents

    def _get_parts(self):
        if not self.ks:
            raise CreatorError("Failed to get partition info, "
                               "please check your kickstart setting.")

        # Set a default partition if no partition is given out
        if not self.ks.handler.partition.partitions:
            partstr = "part / --size 1900 --ondisk sda --fstype=ext3"
            args = partstr.split()
            pd = self.ks.handler.partition.parse(args[1:])
            if pd not in self.ks.handler.partition.partitions:
                self.ks.handler.partition.partitions.append(pd)

        # partitions list from kickstart file
        return kickstart.get_partitions(self.ks)

    def get_disk_names(self):
        """ Returns a list of physical target disk names (e.g., 'sdb') which
        will be created. """

        if self._disk_names:
            return self._disk_names

        #get partition info from ks handler
        parts = self._get_parts()

        for i in range(len(parts)):
            if parts[i].disk:
                disk_name = parts[i].disk
            else:
                raise CreatorError("Failed to create disks, no --ondisk "
                                   "specified in partition line of ks file")

            if parts[i].mountpoint and not parts[i].fstype:
                raise CreatorError("Failed to create disks, no --fstype "
                                    "specified for partition with mountpoint "
                                    "'%s' in the ks file")

            self._disk_names.append(disk_name)

        return self._disk_names

    def _full_name(self, name, extention):
        """ Construct full file name for a file we generate. """
        return "%s-%s.%s" % (self.name, name, extention)

    def _full_path(self, path, name, extention):
        """ Construct full file path to a file we generate. """
        return os.path.join(path, self._full_name(name, extention))

    def get_boot_type(self):
        """ Determine the boot type from fstype and mountpoint. """
        parts = self._get_parts()

        boot_type = ""

        for p in parts:
            if p.mountpoint == "/boot":
                if p.fstype == "msdos":
                    boot_type = "pcbios"
                else:
                    boot_type = p.fstype
        return boot_type

    #
    # Actual implemention
    #
    def _mount_instroot(self, base_on = None):
        """
        For 'wic', we already have our build artifacts and don't want
        to loop mount anything to install into, we just create
        filesystems from the artifacts directly and combine them into
        a partitioned image.

        We still want to reuse as much of the basic mic machinery
        though; despite the fact that we don't actually do loop or any
        other kind of mounting we still want to do many of the same
        things to prepare images, so we basically just adapt to the
        basic framework and reinterpret what 'mounting' means in our
        context.

        _instroot would normally be something like
        /var/tmp/wic/build/imgcreate-s_9AKQ/install_root, for
        installing packages, etc.  We don't currently need to do that,
        so we simplify life by just using /var/tmp/wic/build as our
        workdir.
        """
        parts = self._get_parts()

        self.__instimage = PartitionedMount(self._instroot)

        fstab = self.__write_fstab()

        self.boot_type = self.get_boot_type()

        if not self.bootimg_dir:
            if self.boot_type == "pcbios":
                self.bootimg_dir = self.staging_data_dir
            elif self.boot_type == "efi":
                self.bootimg_dir = self.hdddir

        if self.boot_type == "pcbios":
            self._create_syslinux_config()
        elif self.boot_type == "efi":
            self._create_grubefi_config()
        else:
            raise CreatorError("Failed to detect boot type (no /boot partition?), "
                               "please check your kickstart setting.")

        for p in parts:
            if p.fstype == "efi":
                p.fstype = "msdos"
            # need to create the filesystems in order to get their
            # sizes before we can add them and do the layout.
            # PartitionedMount.mount() actually calls __format_disks()
            # to create the disk images and carve out the partitions,
            # then self.install() calls PartitionedMount.install()
            # which calls __install_partitition() for each partition
            # to dd the fs into the partitions.  It would be nice to
            # be able to use e.g. ExtDiskMount etc to create the
            # filesystems, since that's where existing e.g. mkfs code
            # is, but those are only created after __format_disks()
            # which needs the partition sizes so needs them created
            # before its called.  Well, the existing setup is geared
            # to installing packages into mounted filesystems - maybe
            # when/if we need to actually do package selection we
            # should modify things to use those objects, but for now
            # we can avoid that.
            p.prepare(self.workdir, self.oe_builddir, self.boot_type,
                      self.rootfs_dir, self.bootimg_dir, self.kernel_dir,
                      self.native_sysroot)

            self.__instimage.add_partition(int(p.size),
                                           p.disk,
                                           p.mountpoint,
                                           p.source_file,
                                           p.fstype,
                                           p.label,
                                           fsopts = p.fsopts,
                                           boot = p.active,
                                           align = p.align,
                                           part_type = p.part_type)
        self._restore_fstab(fstab)
        self.__instimage.layout_partitions(self._ptable_format)

        self.__imgdir = self.workdir
        for disk_name, disk in self.__instimage.disks.items():
            full_path = self._full_path(self.__imgdir, disk_name, "direct")
            msger.debug("Adding disk %s as %s with size %s bytes" \
                        % (disk_name, full_path, disk['min_size']))
            disk_obj = fs_related.DiskImage(full_path, disk['min_size'])
            self.__disks[disk_name] = disk_obj
            self.__instimage.add_disk(disk_name, disk_obj)

        self.__instimage.mount()

    def install(self, repo_urls=None):
        """
        Install fs images into partitions
        """
        for disk_name, disk in self.__instimage.disks.items():
            full_path = self._full_path(self.__imgdir, disk_name, "direct")
            msger.debug("Installing disk %s as %s with size %s bytes" \
                        % (disk_name, full_path, disk['min_size']))
            self.__instimage.install(full_path)

    def configure(self, repodata = None):
        """
        Configure the system image according to kickstart.

        For now, it just prepares the image to be bootable by e.g.
        creating and installing a bootloader configuration.
        """
        if self.boot_type == "pcbios":
            self._install_syslinux()

    def print_outimage_info(self):
        """
        Print the image(s) and artifacts used, for the user.
        """
        msg = "The new image(s) can be found here:\n"

        for disk_name, disk in self.__instimage.disks.items():
            full_path = self._full_path(self.__imgdir, disk_name, "direct")
            msg += '  %s\n\n' % full_path

        msg += 'The following build artifacts were used to create the image(s):\n'
        msg += '  ROOTFS_DIR:      %s\n' % self.rootfs_dir
        msg += '  BOOTIMG_DIR:     %s\n' % self.bootimg_dir
        msg += '  KERNEL_DIR:      %s\n' % self.kernel_dir
        msg += '  NATIVE_SYSROOT:  %s\n' % self.native_sysroot

        msger.info(msg)

    def _get_boot_config(self):
        """
        Return the rootdev/root_part_uuid (if specified by
        --part-type)

        Assume partition order same as in wks
        """
        rootdev = None
        root_part_uuid = None
        parts = self._get_parts()
        for num, p in enumerate(parts, 1):
            if p.mountpoint == "/":
                if self._ptable_format == 'msdos' and num > 3:
                    rootdev = "/dev/%s%-d" % (p.disk, num + 1)
                else:
                    rootdev = "/dev/%s%-d" % (p.disk, num)
                root_part_uuid = p.part_type

        return (rootdev, root_part_uuid)

    def _create_syslinux_config(self):
        hdddir = "%s/hdd/boot" % self.workdir
        rm_cmd = "rm -rf " + self.workdir
        exec_cmd(rm_cmd)

        install_cmd = "install -d %s" % hdddir
        tmp = exec_cmd(install_cmd)

        splash = os.path.join(self.workdir, "/hdd/boot/splash.jpg")
        if os.path.exists(splash):
            splashline = "menu background splash.jpg"
        else:
            splashline = ""

        (rootdev, root_part_uuid) = self._get_boot_config()
        options = self.ks.handler.bootloader.appendLine

        syslinux_conf = ""
        syslinux_conf += "PROMPT 0\n"
        timeout = kickstart.get_timeout(self.ks)
        if not timeout:
            timeout = 0
        syslinux_conf += "TIMEOUT " + str(timeout) + "\n"
        syslinux_conf += "\n"
        syslinux_conf += "ALLOWOPTIONS 1\n"
        syslinux_conf += "SERIAL 0 115200\n"
        syslinux_conf += "\n"
        if splashline:
            syslinux_conf += "%s\n" % splashline
        syslinux_conf += "DEFAULT boot\n"
        syslinux_conf += "LABEL boot\n"

        kernel = "/vmlinuz"
        syslinux_conf += "KERNEL " + kernel + "\n"

        if self._ptable_format == 'msdos':
            rootstr = rootdev
        else:
            if not root_part_uuid:
                raise MountError("Cannot find the root GPT partition UUID")
            rootstr = "PARTUUID=%s" % root_part_uuid

        syslinux_conf += "APPEND label=boot root=%s %s\n" % (rootstr, options)

        msger.debug("Writing syslinux config %s/hdd/boot/syslinux.cfg" \
                    % self.workdir)
        cfg = open("%s/hdd/boot/syslinux.cfg" % self.workdir, "w")
        cfg.write(syslinux_conf)
        cfg.close()

    def _create_grubefi_config(self):
        hdddir = "%s/hdd/boot" % self.workdir
        rm_cmd = "rm -rf %s" % self.workdir
        exec_cmd(rm_cmd)

        install_cmd = "install -d %s/EFI/BOOT" % hdddir
        tmp = exec_cmd(install_cmd)

        splash = os.path.join(self.workdir, "/EFI/boot/splash.jpg")
        if os.path.exists(splash):
            splashline = "menu background splash.jpg"
        else:
            splashline = ""

        (rootdev, root_part_uuid) = self._get_boot_config()
        options = self.ks.handler.bootloader.appendLine

        grubefi_conf = ""
        grubefi_conf += "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1\n"
        grubefi_conf += "default=boot\n"
        timeout = kickstart.get_timeout(self.ks)
        if not timeout:
            timeout = 0
        grubefi_conf += "timeout=%s\n" % timeout
        grubefi_conf += "menuentry 'boot'{\n"

        kernel = "/vmlinuz"

        if self._ptable_format == 'msdos':
            rootstr = rootdev
        else:
            if not root_part_uuid:
                raise MountError("Cannot find the root GPT partition UUID")
            rootstr = "PARTUUID=%s" % root_part_uuid

        grubefi_conf += "linux %s root=%s rootwait %s\n" \
            % (kernel, rootstr, options)
        grubefi_conf += "}\n"
        if splashline:
            syslinux_conf += "%s\n" % splashline

        msger.debug("Writing grubefi config %s/hdd/boot/EFI/BOOT/grub.cfg" \
                        % self.workdir)
        cfg = open("%s/hdd/boot/EFI/BOOT/grub.cfg" % self.workdir, "w")
        cfg.write(grubefi_conf)
        cfg.close()

    def _install_syslinux(self):
        mbrfile = "%s/syslinux/" % self.bootimg_dir
        if self._ptable_format == 'gpt':
            mbrfile += "gptmbr.bin"
        else:
            mbrfile += "mbr.bin"

        if not os.path.exists(mbrfile):
            msger.error("Couldn't find %s.  If using the -e option, do you have the right MACHINE set in local.conf?  If not, is the bootimg_dir path correct?" % mbrfile)

        for disk_name, disk in self.__instimage.disks.items():
            full_path = self._full_path(self.__imgdir, disk_name, "direct")
            msger.debug("Installing MBR on disk %s as %s with size %s bytes" \
                            % (disk_name, full_path, disk['min_size']))

            rc = runner.show(['dd', 'if=%s' % mbrfile,
                              'of=%s' % full_path, 'conv=notrunc'])
            if rc != 0:
                raise MountError("Unable to set MBR to %s" % full_path)

    def _unmount_instroot(self):
        if not self.__instimage is None:
            try:
                self.__instimage.cleanup()
            except MountError, err:
                msger.warning("%s" % err)

