#!/usr/bin/env python
# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2015, Intel Corporation.
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
# AUTHORS
# Ed Bartosh <ed.bartosh@linux.intel.com>

"""Test cases for wic."""

import sys

from glob import glob
from shutil import rmtree

from oeqa.selftest.base import oeSelfTest
from oeqa.utils.commands import runCmd, bitbake

class Wic(oeSelfTest):
    """Wic test class."""

    resultdir = "/var/tmp/wic/build/"

    @classmethod
    def setUpClass(cls):
        """Build wic runtime dependencies and images used in the tests."""
        bitbake('syslinux syslinux-native parted-native '
                'dosfstools-native mtools-native core-image-minimal')

    def setUp(self):
        """This code is executed before each test method."""
        rmtree(self.resultdir, ignore_errors=True)

    def test01_help(self):
        """Test wic --help"""
        self.assertEqual(0, runCmd('wic --help').status)

    def test02_createhelp(self):
        """Test wic create --help"""
        self.assertEqual(0, runCmd('wic creat --help').status)

    def test03_listhelp(self):
        """Test wic list --help"""
        self.assertEqual(0, runCmd('wic list --help').status)

    def test04_build_image_name(self):
        """Test wic create directdisk --image-name core-image-minimal"""
        self.assertEqual(0, runCmd("wic create directdisk "
                                   "--image-name core-image-minimal").status)
        self.assertEqual(1, len(glob(self.resultdir + "directdisk-*.direct")))

    def test05_build_artifacts(self):
        """Test wic create directdisk providing all artifacts."""
        self.assertEqual(0, runCmd("wic create directdisk "
                                   "-b tmp/sysroots/qemux86/usr/share "
                                   "-k tmp/deploy/images/qemux86 "
                                   "-n tmp/sysroots/x86_64-linux "
                                   "-r tmp/work/qemux86-poky-linux/"
                                   "core-image-minimal/1.0-r0/rootfs").status)
        self.assertEqual(1, len(glob(self.resultdir + "directdisk-*.direct")))
