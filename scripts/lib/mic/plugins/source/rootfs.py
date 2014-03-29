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
# This implements the 'rootfs' source plugin class for 'wic'
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
# Joao Henrique Ferreira de Freitas <joaohf (at] gmail.com>
#

import os
import shutil
import re
import tempfile

from mic import kickstart, chroot, msger
from mic.utils import misc, fs_related, errors, runner, cmdln
from mic.conf import configmgr
from mic.plugin import pluginmgr
from mic.utils.partitionedfs import PartitionedMount
import mic.imager.direct as direct
from mic.pluginbase import SourcePlugin
from mic.utils.oe.misc import *
from mic.imager.direct import DirectImageCreator

class RootfsPlugin(SourcePlugin):
    name = 'rootfs'

    @classmethod
    def do_prepare_partition(self, part, cr, cr_workdir, oe_builddir, bootimg_dir,
                             kernel_dir, krootfs_dir, native_sysroot):
        """
        Called to do the actual content population for a partition i.e. it
        'prepares' the partition to be incorporated into the image.
        In this case, prepare content for legacy bios boot partition.
        """
        if part.rootfs is None:
            if not 'ROOTFS_DIR' in krootfs_dir:
                msg = "Couldn't find --rootfs-dir, exiting"
                msger.error(msg)
            rootfs_dir = krootfs_dir['ROOTFS_DIR']
        else:
            if part.rootfs in krootfs_dir:
                rootfs_dir = krootfs_dir[part.rootfs]
            elif os.path.isdir(part.rootfs):
                rootfs_dir = part.rootfs
            else:
                msg = "Couldn't find --rootfs-dir=%s connection"
                msg += " or it is not a valid path, exiting"
                msger.error(msg % part.rootfs)

        part.set_rootfs(rootfs_dir)
        part.prepare_rootfs(cr_workdir, oe_builddir, rootfs_dir, native_sysroot)

