#!/usr/bin/python -tt
#
# Copyright (c) 2007, Red Hat, Inc.
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

from __future__ import with_statement
import os
import sys
import errno
import stat
import random
import string
import time
import uuid

from mic import msger
from mic.utils import runner
from mic.utils.errors import *
from mic.utils.oe.misc import *

def find_binary_path(binary):
    if os.environ.has_key("PATH"):
        paths = os.environ["PATH"].split(":")
    else:
        paths = []
        if os.environ.has_key("HOME"):
            paths += [os.environ["HOME"] + "/bin"]
        paths += ["/usr/local/sbin", "/usr/local/bin", "/usr/sbin", "/usr/bin", "/sbin", "/bin"]

    for path in paths:
        bin_path = "%s/%s" % (path, binary)
        if os.path.exists(bin_path):
            return bin_path

    print "External command '%s' not found, exiting." % binary
    print "  (Please install '%s' on your host system)" % binary
    sys.exit(1)

def makedirs(dirname):
    """A version of os.makedirs() that doesn't throw an
    exception if the leaf directory already exists.
    """
    try:
        os.makedirs(dirname)
    except OSError, err:
        if err.errno != errno.EEXIST:
            raise

class Disk:
    """Generic base object for a disk

    The 'create' method must make the disk visible as a block device - eg
    by calling losetup. For RawDisk, this is obviously a no-op. The 'cleanup'
    method must undo the 'create' operation.
    """
    def __init__(self, size, device = None):
        self._device = device
        self._size = size

    def create(self):
        pass

    def cleanup(self):
        pass

    def get_device(self):
        return self._device
    def set_device(self, path):
        self._device = path
    device = property(get_device, set_device)

    def get_size(self):
        return self._size
    size = property(get_size)


class DiskImage(Disk):
    """
    A Disk backed by a file.
    """
    def __init__(self, image_file, size):
        Disk.__init__(self, size)
        self.image_file = image_file

    def exists(self):
        return os.path.exists(self.image_file)

    def create(self):
        if self.device is not None:
            return

        blocks = self.size / 1024
        if self.size - blocks * 1024:
            blocks += 1

        # create disk image
        dd_cmd = "dd if=/dev/zero of=%s bs=1024 seek=%d count=1" % \
            (self.image_file, blocks)
        exec_cmd(dd_cmd)

        self.device = self.image_file
