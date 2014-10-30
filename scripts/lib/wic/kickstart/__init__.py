#!/usr/bin/env python -tt
#
# Copyright (c) 2007 Red Hat, Inc.
# Copyright (c) 2009, 2010, 2011 Intel, Inc.
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

import os, sys, re
import shutil
import subprocess
import string

import pykickstart.sections as kssections
import pykickstart.commands as kscommands
import pykickstart.constants as ksconstants
import pykickstart.errors as kserrors
import pykickstart.parser as ksparser
import pykickstart.version as ksversion
from pykickstart.handlers.control import commandMap
from pykickstart.handlers.control import dataMap

from wic import msger
from wic.utils import errors, misc, runner, fs_related as fs
from custom_commands import wicboot, partition

def read_kickstart(path):
    """Parse a kickstart file and return a KickstartParser instance.

    This is a simple utility function which takes a path to a kickstart file,
    parses it and returns a pykickstart KickstartParser instance which can
    be then passed to an ImageCreator constructor.

    If an error occurs, a CreatorError exception is thrown.
    """

    #version = ksversion.makeVersion()
    #ks = ksparser.KickstartParser(version)

    using_version = ksversion.DEVEL
    commandMap[using_version]["bootloader"] = wicboot.Wic_Bootloader
    commandMap[using_version]["part"] = partition.Wic_Partition
    commandMap[using_version]["partition"] = partition.Wic_Partition
    dataMap[using_version]["PartData"] = partition.Wic_PartData
    superclass = ksversion.returnClassForVersion(version=using_version)

    class KSHandlers(superclass):
        def __init__(self):
            superclass.__init__(self, mapping=commandMap[using_version])

    ks = ksparser.KickstartParser(KSHandlers(), errorsAreFatal=False)

    try:
        ks.readKickstart(path)
    except (kserrors.KickstartParseError, kserrors.KickstartError), err:
        if msger.ask("Errors occured on kickstart file, skip and continue?"):
            msger.warning("%s" % err)
            pass
        else:
            raise errors.KsError("%s" % err)

    return ks

def get_image_size(ks, default = None):
    __size = 0
    for p in ks.handler.partition.partitions:
        if p.mountpoint == "/" and p.size:
            __size = p.size
    if __size > 0:
        return int(__size) * 1024L * 1024L
    else:
        return default

def get_image_fstype(ks, default = None):
    for p in ks.handler.partition.partitions:
        if p.mountpoint == "/" and p.fstype:
            return p.fstype
    return default

def get_image_fsopts(ks, default = None):
    for p in ks.handler.partition.partitions:
        if p.mountpoint == "/" and p.fsopts:
            return p.fsopts
    return default

def get_timeout(ks, default = None):
    if not hasattr(ks.handler.bootloader, "timeout"):
        return default
    if ks.handler.bootloader.timeout is None:
        return default
    return int(ks.handler.bootloader.timeout)

def get_kernel_args(ks, default = "ro rd.live.image"):
    if not hasattr(ks.handler.bootloader, "appendLine"):
        return default
    if ks.handler.bootloader.appendLine is None:
        return default
    return "%s %s" %(default, ks.handler.bootloader.appendLine)

def get_menu_args(ks, default = ""):
    if not hasattr(ks.handler.bootloader, "menus"):
        return default
    if ks.handler.bootloader.menus in (None, ""):
        return default
    return "%s" % ks.handler.bootloader.menus

def get_default_kernel(ks, default = None):
    if not hasattr(ks.handler.bootloader, "default"):
        return default
    if not ks.handler.bootloader.default:
        return default
    return ks.handler.bootloader.default

def get_partitions(ks):
    return ks.handler.partition.partitions
