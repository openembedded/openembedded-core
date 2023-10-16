# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# patchtestpatch: PatchTestPatch class which abstracts a patch file
#
# Copyright (C) 2016 Intel Corporation
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

import logging
import utils

logger = logging.getLogger('patchtest')

class PatchTestPatch(object):
    MERGE_STATUS_INVALID = 'INVALID'
    MERGE_STATUS_NOT_MERGED = 'NOTMERGED'
    MERGE_STATUS_MERGED_SUCCESSFULL = 'PASS'
    MERGE_STATUS_MERGED_FAIL = 'FAIL'
    MERGE_STATUS = (MERGE_STATUS_INVALID,
                    MERGE_STATUS_NOT_MERGED,
                    MERGE_STATUS_MERGED_SUCCESSFULL,
                    MERGE_STATUS_MERGED_FAIL)

    def __init__(self, path, forcereload=False):
        self._path = path
        self._forcereload = forcereload

        self._contents = None
        self._branch = None
        self._merge_status = PatchTestPatch.MERGE_STATUS_NOT_MERGED

    @property
    def contents(self):
        if self._forcereload or (not self._contents):
            logger.debug('Reading %s contents' % self._path)
            try:
                with open(self._path, newline='') as _f:
                    self._contents = _f.read()
            except IOError:
                logger.warn("Reading the mbox %s failed" % self.resource)
        return self._contents

    @property
    def path(self):
        return self._path

    @property
    def branch(self):
        if not self._branch:
            self._branch = utils.get_branch(self._path)
        return self._branch

    def setmergestatus(self, status):
        self._merge_status = status

    def getmergestatus(self):
        return self._merge_status

    merge_status = property(getmergestatus, setmergestatus)

