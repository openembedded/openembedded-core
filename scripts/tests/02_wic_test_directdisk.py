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

"""Test directdisk builds."""

import imp
import unittest
import os
import shutil
import glob

class TestWicDirectdisk(unittest.TestCase):
    """Test build of directdisk image"""
    def __init__(self, methodName='runTest'):
        unittest.TestCase.__init__(self, methodName)
        self.main = imp.load_source("wic", "wic").main
        self.resultdir = "/var/tmp/wic/build/"

    def setUp(self):
        """This code executes before each test method."""
        shutil.rmtree(self.resultdir)
        os.chdir(os.getenv("BUILDDIR"))

    def _build(self, cmdline):
        """Call self.main with provided commandline."""
        self.assertIsNone(self.main(cmdline))
        self.assertEqual(1, len(glob.glob(self.resultdir + \
                                "directdisk-*.direct")))

    def testbuild_image_name(self):
        self._build(["create", "directdisk",
                     "--image-name", "core-image-minimal"])

    def testbuild_artifacts(self):
        self._build(["create", "directdisk",
                     "-b", "tmp/sysroots/qemux86/usr/share",
                     "-k", "tmp/deploy/images/qemux86",
                     "-n", "tmp/sysroots/x86_64-linux",
                     "-r", "tmp/work/qemux86-poky-linux/"
                           "core-image-minimal/1.0-r0/rootfs"])

