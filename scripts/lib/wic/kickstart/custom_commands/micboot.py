#!/usr/bin/env python -tt
#
# Copyright (c) 2008, 2009, 2010 Intel, Inc.
#
# Anas Nashif
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of the GNU General Public License as published by the Free
# Software Foundation; version 2 of the License
#
# This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
# or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
# for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc., 59
# Temple Place - Suite 330, Boston, MA 02111-1307, USA.

from pykickstart.base import *
from pykickstart.errors import *
from pykickstart.options import *
from pykickstart.commands.bootloader import *

class Mic_Bootloader(F8_Bootloader):
    def __init__(self, writePriority=10, appendLine="", driveorder=None,
                 forceLBA=False, location="", md5pass="", password="",
                 upgrade=False, menus=""):
        F8_Bootloader.__init__(self, writePriority, appendLine, driveorder,
                                forceLBA, location, md5pass, password, upgrade)

        self.menus = ""
        self.ptable = "msdos"

    def _getArgsAsStr(self):
        ret = F8_Bootloader._getArgsAsStr(self)

        if self.menus == "":
            ret += " --menus=%s" %(self.menus,)
        if self.ptable:
            ret += " --ptable=\"%s\"" %(self.ptable,)
        return ret

    def _getParser(self):
        op = F8_Bootloader._getParser(self)
        op.add_option("--menus", dest="menus")
        op.add_option("--ptable", dest="ptable", type="string")
        return op

