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
# This module provides the OpenEmbedded bootloader object definitions.
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#

from pykickstart.base import *
from pykickstart.errors import *
from pykickstart.options import *
from pykickstart.commands.bootloader import *

from wic.kickstart.custom_commands.micboot import *

class Wic_Bootloader(Mic_Bootloader):
    def __init__(self, writePriority=10, appendLine="", driveorder=None,
                 forceLBA=False, location="", md5pass="", password="",
                 upgrade=False, menus=""):
        Mic_Bootloader.__init__(self, writePriority, appendLine, driveorder,
                                forceLBA, location, md5pass, password, upgrade)

        self.source = ""

    def _getArgsAsStr(self):
        retval = Mic_Bootloader._getArgsAsStr(self)

        if self.source:
            retval += " --source=%s" % self.source

        return retval

    def _getParser(self):
        op = Mic_Bootloader._getParser(self)
        # use specified source plugin to implement bootloader-specific methods
        op.add_option("--source", type="string", action="store",
                      dest="source", default=None)
        return op

