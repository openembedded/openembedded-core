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
from oeqa.utils.commands import runCmd, bitbake, get_bb_var

class Wic(oeSelfTest):
    """Wic test class."""

    resultdir = "/var/tmp/wic/build/"

    @classmethod
    def setUpClass(cls):
        """Build wic runtime dependencies and images used in the tests."""
        bitbake('syslinux syslinux-native parted-native gptfdisk-native '
                'dosfstools-native mtools-native core-image-minimal')

    def setUp(self):
        """This code is executed before each test method."""
        rmtree(self.resultdir, ignore_errors=True)

    def test01_help(self):
        """Test wic --help"""
        self.assertEqual(0, runCmd('wic --help').status)

    def test02_createhelp(self):
        """Test wic create --help"""
        self.assertEqual(0, runCmd('wic create --help').status)

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
        vars = dict((var.lower(), get_bb_var(var, 'core-image-minimal')) \
                        for var in ('STAGING_DATADIR', 'DEPLOY_DIR_IMAGE',
                                    'STAGING_DIR_NATIVE', 'IMAGE_ROOTFS'))
        status = runCmd("wic create directdisk "
                        "-b %(staging_datadir)s "
                        "-k %(deploy_dir_image)s "
                        "-n %(staging_dir_native)s "
                        "-r %(image_rootfs)s" % vars).status
        self.assertEqual(0, status)
        self.assertEqual(1, len(glob(self.resultdir + "directdisk-*.direct")))

    def test06_gpt_image(self):
        """Test creation of core-image-minimal with gpt table and UUID boot"""
        self.assertEqual(0, runCmd("wic create directdisk-gpt "
                                   "--image-name core-image-minimal").status)
        self.assertEqual(1, len(glob(self.resultdir + "directdisk-*.direct")))

    def test07_unsupported_subcommand(self):
        """Test unsupported subcommand"""
        self.assertEqual(1, runCmd('wic unsupported',
                         ignore_status=True).status)

    def test08_no_command(self):
        """Test wic without command"""
        self.assertEqual(1, runCmd('wic', ignore_status=True).status)

    def test09_help_kickstart(self):
        """Test wic help overview"""
        self.assertEqual(0, runCmd('wic help overview').status)

    def test10_help_plugins(self):
        """Test wic help plugins"""
        self.assertEqual(0, runCmd('wic help plugins').status)

    def test11_help_kickstart(self):
        """Test wic help kickstart"""
        self.assertEqual(0, runCmd('wic help kickstart').status)

    def test12_compress_gzip(self):
        """Test compressing an image with gzip"""
        self.assertEqual(0, runCmd("wic create directdisk "
                                   "--image-name core-image-minimal "
                                   "-c gzip").status)
        self.assertEqual(1, len(glob(self.resultdir + \
                                         "directdisk-*.direct.gz")))

    def test13_compress_gzip(self):
        """Test compressing an image with bzip2"""
        self.assertEqual(0, runCmd("wic create directdisk "
                                   "--image-name core-image-minimal "
                                   "-c bzip2").status)
        self.assertEqual(1, len(glob(self.resultdir + \
                                         "directdisk-*.direct.bz2")))

    def test14_compress_gzip(self):
        """Test compressing an image with xz"""
        self.assertEqual(0, runCmd("wic create directdisk "
                                   "--image-name core-image-minimal "
                                   "-c xz").status)
        self.assertEqual(1, len(glob(self.resultdir + \
                                         "directdisk-*.direct.xz")))

    def test15_wrong_compressor(self):
        """Test how wic breaks if wrong compressor is provided"""
        self.assertEqual(2, runCmd("wic create directdisk "
                                   "--image-name core-image-minimal "
                                   "-c wrong", ignore_status=True).status)

    def test16_rootfs_indirect_recipes(self):
        """Test usage of rootfs plugin with rootfs recipes"""
        wks = "directdisk-multi-rootfs"
        self.assertEqual(0, runCmd("wic create %s "
                                   "--image-name core-image-minimal "
                                   "--rootfs rootfs1=core-image-minimal "
                                   "--rootfs rootfs2=core-image-minimal" \
                                   % wks).status)
        self.assertEqual(1, len(glob(self.resultdir + "%s*.direct" % wks)))

    def test17_rootfs_artifacts(self):
        """Test usage of rootfs plugin with rootfs paths"""
        vars = dict((var.lower(), get_bb_var(var, 'core-image-minimal')) \
                        for var in ('STAGING_DATADIR', 'DEPLOY_DIR_IMAGE',
                                    'STAGING_DIR_NATIVE', 'IMAGE_ROOTFS'))
        vars['wks'] = "directdisk-multi-rootfs"
        status = runCmd("wic create %(wks)s "
                        "-b %(staging_datadir)s "
                        "-k %(deploy_dir_image)s "
                        "-n %(staging_dir_native)s "
                        "--rootfs-dir rootfs1=%(image_rootfs)s "
                        "--rootfs-dir rootfs2=%(image_rootfs)s" \
                        % vars).status
        self.assertEqual(0, status)
        self.assertEqual(1, len(glob(self.resultdir + \
                                     "%(wks)s-*.direct" % vars)))
