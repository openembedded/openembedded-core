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
# This implements the 'direct' imager plugin class for 'wic', based
# loosely on the raw imager plugin from 'mic'
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

import os
import shutil
import re
import tempfile

from mic import chroot, msger
from mic.utils import misc, fs_related, errors, runner, cmdln
from mic.conf import configmgr
from mic.plugin import pluginmgr
from mic.utils.partitionedfs import PartitionedMount

import mic.imager.direct as direct
from mic.pluginbase import ImagerPlugin

class DirectPlugin(ImagerPlugin):
    name = 'direct'

    @classmethod
    def __rootfs_dir_to_dict(self, rootfs_dirs):
        """
        Gets a string that contain 'connection=dir' splitted by
        space and return a dict
        """
        krootfs_dir = {}
        for rootfs_dir in rootfs_dirs.split(' '):
            k, v = rootfs_dir.split('=')
            krootfs_dir[k] = v

        return krootfs_dir

    @classmethod
    def do_create(self, subcmd, opts, *args):
        """
        Create direct image, called from creator as 'direct' cmd
        """
        if len(args) != 9:
            raise errors.Usage("Extra arguments given")

        staging_data_dir = args[0]
        hdddir = args[1]
        native_sysroot = args[2]
        kernel_dir = args[3]
        bootimg_dir = args[4]
        rootfs_dir = args[5]

        creatoropts = configmgr.create
        ksconf = args[6]

        image_output_dir = args[7]
        oe_builddir = args[8]

        krootfs_dir = self.__rootfs_dir_to_dict(rootfs_dir)

        configmgr._ksconf = ksconf

        creator = direct.DirectImageCreator(oe_builddir,
                                            image_output_dir,
                                            krootfs_dir,
                                            bootimg_dir,
                                            kernel_dir,
                                            native_sysroot,
                                            hdddir,
                                            staging_data_dir,
                                            creatoropts,
                                            None,
                                            None,
                                            None)

        try:
            creator.mount(None, creatoropts["cachedir"])
            creator.install()
            creator.configure(creatoropts["repomd"])
            creator.print_outimage_info()

        except errors.CreatorError:
            raise
        finally:
            creator.cleanup()

        return 0
