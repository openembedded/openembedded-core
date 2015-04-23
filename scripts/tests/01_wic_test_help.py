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

import imp
import unittest

class TestWicHelp(unittest.TestCase):
    """Test help output of wic (sub)commands"""

    def setUp(self):
        """This code is executed before each test method."""
        self.main = imp.load_source("wic", "wic").main

    def testhelp(self):
        """Test wic --help"""
        self.assertRaises(SystemExit, self.main, ['--help'])

    def testcreatehelp(self):
        """Test wic create --help"""
        self.assertRaises(SystemExit, self.main, ['create', '--help'])

    def testlisthelp(self):
        """Test wic list --help"""
        self.assertRaises(SystemExit, self.main, ['list', '--help'])
