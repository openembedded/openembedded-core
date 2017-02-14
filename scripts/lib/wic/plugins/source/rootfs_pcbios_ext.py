# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# This program is free software; you can distribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for mo details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
# AUTHOR
# Adrian Freihofer <adrian.freihofer (at] neratec.com>
#

import logging
import os
import re

from wic import WicError
from wic.utils import runner
from wic.utils.misc import get_bitbake_var, exec_cmd, exec_native_cmd
from wic.pluginbase import SourcePlugin

logger = logging.getLogger('wic')

def serial_console_form_kargs(kernel_args):
    """
    Create SERIAL... line from kernel parameters

    syslinux needs a line SERIAL port [baudrate [flowcontrol]]
    in the syslinux.cfg file. The config line is generated based
    on kernel boot parameters. The the parameters of the first
    ttyS console are considered for syslinux config.
    @param kernel_args kernel command line
    @return line for syslinux config file e.g. "SERIAL 0 115200"
    """
    syslinux_conf = ""
    for param in kernel_args.split():
        param_match = re.match("console=ttyS([0-9]+),?([0-9]*)([noe]?)([0-9]?)(r?)", param)
        if param_match:
            syslinux_conf += "SERIAL " + param_match.group(1)
            # baudrate
            if param_match.group(2):
                syslinux_conf += " " + param_match.group(2)
            # parity
            if param_match.group(3) and param_match.group(3) != 'n':
                logger.warning("syslinux does not support parity for console. "
                               "%s is ignored.", param_match.group(3))
            # number of bits
            if param_match.group(4) and param_match.group(4) != '8':
                logger.warning("syslinux supports 8 bit console configuration "
                               "only. %s is ignored.", param_match.group(4))
            # flow control
            if param_match.group(5) and param_match.group(5) != '':
                logger.warning("syslinux console flowcontrol configuration. "
                               "%s is ignored.", param_match.group(5))
            break

    return syslinux_conf


# pylint: disable=no-init
class RootfsPlugin(SourcePlugin):
    """
    Create root partition and install syslinux bootloader

    This plugin creates a disk image containing a bootable root partition with
    syslinux installed. The filesystem is ext2/3/4, no extra boot partition is
    required.

    Example kickstart file:
    part / --source rootfs-pcbios-ext --ondisk sda --fstype=ext4 --label rootfs --align 1024
    bootloader --source rootfs-pcbios-ext --timeout=0 --append="rootwait rootfstype=ext4"

    The first line generates a root file system including a syslinux.cfg file
    The "--source rootfs-pcbios-ext" in the second line triggers the installation
    of ldlinux.sys into the image.
    """

    name = 'rootfs-pcbios-ext'

    @staticmethod
    def _get_rootfs_dir(rootfs_dir):
        """
        Find rootfs pseudo dir

        If rootfs_dir is a directory consider it as rootfs directory.
        Otherwise ask bitbake about the IMAGE_ROOTFS directory.
        """
        if os.path.isdir(rootfs_dir):
            return rootfs_dir

        image_rootfs_dir = get_bitbake_var("IMAGE_ROOTFS", rootfs_dir)
        if not os.path.isdir(image_rootfs_dir):
            raise WicError("No valid artifact IMAGE_ROOTFS from image named %s "
                           "has been found at %s, exiting." %
                           (rootfs_dir, image_rootfs_dir))

        return image_rootfs_dir

    # pylint: disable=unused-argument
    @classmethod
    def do_configure_partition(cls, part, source_params, image_creator,
                               image_creator_workdir, oe_builddir, bootimg_dir,
                               kernel_dir, native_sysroot):
        """
        Creates syslinux config in rootfs directory

        Called before do_prepare_partition()
        """
        bootloader = image_creator.ks.bootloader

        syslinux_conf = ""
        syslinux_conf += "PROMPT 0\n"

        syslinux_conf += "TIMEOUT " + str(bootloader.timeout) + "\n"
        syslinux_conf += "ALLOWOPTIONS 1\n"

        # Derive SERIAL... line from from kernel boot parameters
        syslinux_conf += serial_console_form_kargs(options) + "\n"

        syslinux_conf += "DEFAULT linux\n"
        syslinux_conf += "LABEL linux\n"
        syslinux_conf += "  KERNEL /boot/bzImage\n"

        syslinux_conf += "  APPEND label=boot root=%s %s\n" % \
                             (image_creator.rootdev, bootloader.append)

        syslinux_cfg = os.path.join(image_creator.rootfs_dir['ROOTFS_DIR'], "boot", "syslinux.cfg")
        logger.debug("Writing syslinux config %s", syslinux_cfg)
        with open(syslinux_cfg, "w") as cfg:
            cfg.write(syslinux_conf)

    @classmethod
    def do_prepare_partition(cls, part, source_params, image_creator,
                             image_creator_workdir, oe_builddir, bootimg_dir,
                             kernel_dir, krootfs_dir, native_sysroot):
        """
        Creates partition out of rootfs directory

        Prepare content for a rootfs partition i.e. create a partition
        and fill it from a /rootfs dir.
        Install syslinux bootloader into root partition image file
        """
        def is_exe(exepath):
            """Verify exepath is an executable file"""
            return os.path.isfile(exepath) and os.access(exepath, os.X_OK)

        # Make sure syslinux-nomtools is available in native sysroot or fail
        native_syslinux_nomtools = os.path.join(native_sysroot, "usr/bin/syslinux-nomtools")
        if not is_exe(native_syslinux_nomtools):
            logger.info("building syslinux-native...")
            exec_cmd("bitbake syslinux-native")
        if not is_exe(native_syslinux_nomtools):
            raise WicError("Couldn't find syslinux-nomtools (%s), exiting" %
                           native_syslinux_nomtools)

        if part.rootfs is None:
            if 'ROOTFS_DIR' not in krootfs_dir:
                raise WicError("Couldn't find --rootfs-dir, exiting")
            rootfs_dir = krootfs_dir['ROOTFS_DIR']
        else:
            if part.rootfs in krootfs_dir:
                rootfs_dir = krootfs_dir[part.rootfs]
            elif part.rootfs:
                rootfs_dir = part.rootfs
            else:
                raise WicError("Couldn't find --rootfs-dir=%s connection or "
                               "it is not a valid path, exiting" % part.rootfs)

        real_rootfs_dir = cls._get_rootfs_dir(rootfs_dir)

        part.rootfs_dir = real_rootfs_dir
        part.prepare_rootfs(image_creator_workdir, oe_builddir, real_rootfs_dir, native_sysroot)

        # install syslinux into rootfs partition
        syslinux_cmd = "syslinux-nomtools -d /boot -i %s" % part.source_file
        exec_native_cmd(syslinux_cmd, native_sysroot)

    @classmethod
    def do_install_disk(cls, disk, disk_name, image_creator, workdir, oe_builddir,
                        bootimg_dir, kernel_dir, native_sysroot):
        """
        Assemble partitions to disk image

        Called after all partitions have been prepared and assembled into a
        disk image. In this case, we install the MBR.
        """
        mbrfile = os.path.join(native_sysroot, "usr/share/syslinux/")
        if image_creator.ptable_format == 'msdos':
            mbrfile += "mbr.bin"
        elif image_creator.ptable_format == 'gpt':
            mbrfile += "gptmbr.bin"
        else:
            raise WicError("Unsupported partition table: %s" %
                           image_creator.ptable_format)

        if not os.path.exists(mbrfile):
            raise WicError("Couldn't find %s. Has syslinux-native been baked?",
                           mbrfile)
        full_path = disk.path
        logger.debug("Installing MBR on disk %s as %s with size %s bytes",
                     disk_name, full_path, disk.min_size)

        ret_code = runner.show(['dd', 'if=%s' % mbrfile, 'of=%s' % full_path, 'conv=notrunc'])
        if ret_code != 0:
            raise WicError("Unable to set MBR to %s" % full_path)
