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
from mic.plugin import pluginmgr

partition_methods = {
    "do_stage_partition":None,
    "do_prepare_partition":None,
    "do_configure_partition":None,
}

class Wic_PartData(Mic_PartData):
    removedKeywords = Mic_PartData.removedKeywords
    removedAttrs = Mic_PartData.removedAttrs

    def __init__(self, *args, **kwargs):
        Mic_PartData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()
        self.source = kwargs.get("source", None)
        self.rootfs = kwargs.get("rootfs-dir", None)
        self.source_file = ""
        self.size = 0

    def _getArgsAsStr(self):
        retval = Mic_PartData._getArgsAsStr(self)

        if self.source:
            retval += " --source=%s" % self.source
            if self.rootfs:
                retval += " --rootfs-dir=%s" % self.rootfs

        return retval

    def get_rootfs(self):
        """
        Acessor for rootfs dir
        """
        return self.rootfs

    def set_rootfs(self, rootfs):
        """
        Acessor for actual rootfs dir, which must be set by source
        plugins.
        """
        self.rootfs = rootfs

    def get_size(self):
        """
        Accessor for partition size, 0 or --size before set_size().
        """
        return self.size

    def set_size(self, size):
        """
        Accessor for actual partition size, which must be set by source
        plugins.
        """
        self.size = size

    def set_source_file(self, source_file):
        """
        Accessor for source_file, the location of the generated partition
        image, which must be set by source plugins.
        """
        self.source_file = source_file

    def get_extra_block_count(self, current_blocks):
        """
        The --size param is reflected in self.size (in MB), and we already
        have current_blocks (1k) blocks, calculate and return the
        number of (1k) blocks we need to add to get to --size, 0 if
        we're already there or beyond.
        """
        msger.debug("Requested partition size for %s: %d" % \
                    (self.mountpoint, self.size))

        if not self.size:
            return 0

        requested_blocks = self.size * 1024

        msger.debug("Requested blocks %d, current_blocks %d" % \
                    (requested_blocks, current_blocks))

        if requested_blocks > current_blocks:
            return requested_blocks - current_blocks
        else:
            return 0

    def prepare(self, cr, cr_workdir, oe_builddir, rootfs_dir, bootimg_dir,
                kernel_dir, native_sysroot):
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

        self._source_methods = pluginmgr.get_source_plugin_methods(self.source, partition_methods)
        self._source_methods["do_configure_partition"](self, cr, cr_workdir,
                                                       oe_builddir,
                                                       bootimg_dir,
                                                       kernel_dir,
                                                       native_sysroot)
        self._source_methods["do_stage_partition"](self, cr, cr_workdir,
                                                   oe_builddir,
                                                   bootimg_dir, kernel_dir,
                                                   native_sysroot)
        self._source_methods["do_prepare_partition"](self, cr, cr_workdir,
                                                     oe_builddir,
                                                     bootimg_dir, kernel_dir, rootfs_dir,
                                                     native_sysroot)

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

        image_rootfs = rootfs_dir
        rootfs = "%s/rootfs_%s.%s" % (cr_workdir, self.label ,self.fstype)

        du_cmd = "du -ks %s" % image_rootfs
        rc, out = exec_cmd(du_cmd)
        actual_rootfs_size = int(out.split()[0])

        extra_blocks = self.get_extra_block_count(actual_rootfs_size)

        if extra_blocks < IMAGE_EXTRA_SPACE:
            extra_blocks = IMAGE_EXTRA_SPACE

        rootfs_size = actual_rootfs_size + extra_blocks

        msger.debug("Added %d extra blocks to %s to get to %d total blocks" % \
                    (extra_blocks, self.mountpoint, rootfs_size))

        dd_cmd = "dd if=/dev/zero of=%s bs=1024 seek=%d count=0 bs=1k" % \
            (rootfs, rootfs_size)
        rc, out = exec_cmd(dd_cmd)

        extra_imagecmd = "-i 8192"

        mkfs_cmd = "mkfs.%s -F %s %s -d %s" % \
            (self.fstype, extra_imagecmd, rootfs, image_rootfs)
        rc, out = exec_native_cmd(pseudo + mkfs_cmd, native_sysroot)


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
        image_rootfs = rootfs_dir
        rootfs = "%s/rootfs_%s.%s" % (cr_workdir, self.label, self.fstype)

        du_cmd = "du -ks %s" % image_rootfs
        rc, out = exec_cmd(du_cmd)
        actual_rootfs_size = int(out.split()[0])

        extra_blocks = self.get_extra_block_count(actual_rootfs_size)

        if extra_blocks < IMAGE_EXTRA_SPACE:
            extra_blocks = IMAGE_EXTRA_SPACE

        rootfs_size = actual_rootfs_size + extra_blocks

        msger.debug("Added %d extra blocks to %s to get to %d total blocks" % \
                    (extra_blocks, self.mountpoint, rootfs_size))

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
        # use specified rootfs path to fill the partition
        op.add_option("--rootfs-dir", type="string", action="store",
                      dest="rootfs", default=None)
        return op
