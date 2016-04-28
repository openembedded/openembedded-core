#!/usr/bin/env python -tt
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

from wic.utils.oe.misc import exec_cmd

class DiskImage():
    """
    A Disk backed by a file.
    """
    def __init__(self, device, size):
        self.size = size
        self.device = device
        self.created = False

    def exists(self):
        return os.path.exists(self.device)

    def create(self):
        if self.created:
            return
        # create sparse disk image
        cmd = "truncate %s -s %s" % (self.device, self.size)
        exec_cmd(cmd)
        self.created = True
