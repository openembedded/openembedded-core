# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2014, Intel Corporation.
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
# This implements the 'bootimg-efi' source plugin class for 'wic'
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

import os
import shutil
import re
import tempfile

from wic import kickstart, msger
from wic.utils import misc, fs_related, errors, runner, cmdln
from wic.conf import configmgr
from wic.plugin import pluginmgr
import wic.imager.direct as direct
from wic.pluginbase import SourcePlugin
from wic.utils.oe.misc import *
from wic.imager.direct import DirectImageCreator

class BootimgEFIPlugin(SourcePlugin):
    name = 'bootimg-efi'

    @classmethod
    def do_configure_grubefi(self, hdddir, cr, cr_workdir):
        """
        Create loader-specific (grub-efi) config
        """
        splash = os.path.join(cr_workdir, "/EFI/boot/splash.jpg")
        if os.path.exists(splash):
            splashline = "menu background splash.jpg"
        else:
            splashline = ""

        (rootdev, root_part_uuid) = cr._get_boot_config()
        options = cr.ks.handler.bootloader.appendLine

        grubefi_conf = ""
        grubefi_conf += "serial --unit=0 --speed=115200 --word=8 --parity=no --stop=1\n"
        grubefi_conf += "default=boot\n"
        timeout = kickstart.get_timeout(cr.ks)
        if not timeout:
            timeout = 0
        grubefi_conf += "timeout=%s\n" % timeout
        grubefi_conf += "menuentry 'boot'{\n"

        kernel = "/bzImage"

        if cr._ptable_format == 'msdos':
            rootstr = rootdev
        else:
            raise ImageError("Unsupported partition table format found")

        grubefi_conf += "linux %s root=%s rootwait %s\n" \
            % (kernel, rootstr, options)
        grubefi_conf += "}\n"
        if splashline:
            syslinux_conf += "%s\n" % splashline

        msger.debug("Writing grubefi config %s/hdd/boot/EFI/BOOT/grub.cfg" \
                        % cr_workdir)
        cfg = open("%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir, "w")
        cfg.write(grubefi_conf)
        cfg.close()

    @classmethod
    def do_configure_gummiboot(self, hdddir, cr, cr_workdir):
        """
        Create loader-specific (gummiboot) config
        """
        install_cmd = "install -d %s/loader" % hdddir
        exec_cmd(install_cmd)

        install_cmd = "install -d %s/loader/entries" % hdddir
        exec_cmd(install_cmd)

        (rootdev, root_part_uuid) = cr._get_boot_config()
        options = cr.ks.handler.bootloader.appendLine

        timeout = kickstart.get_timeout(cr.ks)
        if not timeout:
            timeout = 0

        loader_conf = ""
        loader_conf += "default boot\n"
        loader_conf += "timeout %d\n" % timeout

        msger.debug("Writing gummiboot config %s/hdd/boot/loader/loader.conf" \
                        % cr_workdir)
        cfg = open("%s/hdd/boot/loader/loader.conf" % cr_workdir, "w")
        cfg.write(loader_conf)
        cfg.close()

        kernel = "/bzImage"

        if cr._ptable_format == 'msdos':
            rootstr = rootdev
        else:
            raise ImageError("Unsupported partition table format found")

        boot_conf = ""
        boot_conf += "title boot\n"
        boot_conf += "linux %s\n" % kernel
        boot_conf += "options LABEL=Boot root=%s %s\n" \
            % (rootstr, options)

        msger.debug("Writing gummiboot config %s/hdd/boot/loader/entries/boot.conf" \
                        % cr_workdir)
        cfg = open("%s/hdd/boot/loader/entries/boot.conf" % cr_workdir, "w")
        cfg.write(boot_conf)
        cfg.close()


    @classmethod
    def do_configure_partition(self, part, source_params, cr, cr_workdir,
                               oe_builddir, bootimg_dir, kernel_dir,
                               native_sysroot):
        """
        Called before do_prepare_partition(), creates loader-specific config
        """
        hdddir = "%s/hdd/boot" % cr_workdir
        rm_cmd = "rm -rf %s" % cr_workdir
        exec_cmd(rm_cmd)

        install_cmd = "install -d %s/EFI/BOOT" % hdddir
        exec_cmd(install_cmd)

        try:
            if source_params['loader'] == 'grub-efi':
                self.do_configure_grubefi(hdddir, cr, cr_workdir)
            elif source_params['loader'] == 'gummiboot':
                self.do_configure_gummiboot(hdddir, cr, cr_workdir)
            else:
                msger.error("unrecognized bootimg-efi loader: %s" % source_params['loader'])
        except KeyError:
            msger.error("bootimg-efi requires a loader, none specified")


    @classmethod
    def do_prepare_partition(self, part, source_params, cr, cr_workdir,
                             oe_builddir, bootimg_dir, kernel_dir,
                             rootfs_dir, native_sysroot):
        """
        Called to do the actual content population for a partition i.e. it
        'prepares' the partition to be incorporated into the image.
        In this case, prepare content for an EFI (grub) boot partition.
        """
        if not bootimg_dir:
            bootimg_dir = get_bitbake_var("HDDDIR")
            if not bootimg_dir:
                msger.error("Couldn't find HDDDIR, exiting\n")
            # just so the result notes display it
            cr.set_bootimg_dir(bootimg_dir)

        staging_kernel_dir = kernel_dir

        hdddir = "%s/hdd/boot" % cr_workdir

        install_cmd = "install -m 0644 %s/bzImage %s/bzImage" % \
            (staging_kernel_dir, hdddir)
        exec_cmd(install_cmd)

        try:
            if source_params['loader'] == 'grub-efi':
                shutil.copyfile("%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir,
                                "%s/grub.cfg" % cr_workdir)
                cp_cmd = "cp %s/EFI/BOOT/* %s/EFI/BOOT" % (bootimg_dir, hdddir)
                exec_cmd(cp_cmd, True)
                shutil.move("%s/grub.cfg" % cr_workdir,
                            "%s/hdd/boot/EFI/BOOT/grub.cfg" % cr_workdir)
            elif source_params['loader'] == 'gummiboot':
                cp_cmd = "cp %s/EFI/BOOT/* %s/EFI/BOOT" % (bootimg_dir, hdddir)
                exec_cmd(cp_cmd, True)
            else:
                msger.error("unrecognized bootimg-efi loader: %s" % source_params['loader'])
        except KeyError:
            msger.error("bootimg-efi requires a loader, none specified")

        du_cmd = "du -bks %s" % hdddir
        out = exec_cmd(du_cmd)
        blocks = int(out.split()[0])

        extra_blocks = part.get_extra_block_count(blocks)

        if extra_blocks < BOOTDD_EXTRA_SPACE:
            extra_blocks = BOOTDD_EXTRA_SPACE

        blocks += extra_blocks

        msger.debug("Added %d extra blocks to %s to get to %d total blocks" % \
                    (extra_blocks, part.mountpoint, blocks))

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
        out = exec_cmd(du_cmd)
        bootimg_size = out.split()[0]

        part.set_size(bootimg_size)
        part.set_source_file(bootimg)
