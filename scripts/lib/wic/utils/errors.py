#!/usr/bin/env python -tt
#
# Copyright (c) 2007 Red Hat, Inc.
# Copyright (c) 2011 Intel, Inc.
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

class CreatorError(Exception):
    """An exception base class for all imgcreate errors."""
    keyword = '<creator>'

    def __init__(self, msg):
        self.msg = msg

    def __str__(self):
        if isinstance(self.msg, unicode):
            self.msg = self.msg.encode('utf-8', 'ignore')
        else:
            self.msg = str(self.msg)
        return self.keyword + self.msg

class Usage(CreatorError):
    keyword = '<usage>'

    def __str__(self):
        if isinstance(self.msg, unicode):
            self.msg = self.msg.encode('utf-8', 'ignore')
        else:
            self.msg = str(self.msg)
        return self.keyword + self.msg + ', please use "--help" for more info'

class KsError(CreatorError):
    keyword = '<kickstart>'

class ImageError(CreatorError):
    keyword = '<mount>'
