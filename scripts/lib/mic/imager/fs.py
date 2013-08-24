#!/usr/bin/python -tt
#
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

import os

from mic import msger
from mic.utils import runner, misc
from mic.utils.errors import CreatorError
from mic.utils.fs_related import find_binary_path
from mic.imager.baseimager import BaseImageCreator

class FsImageCreator(BaseImageCreator):
    def __init__(self, cfgmgr = None, pkgmgr = None):
        self.zips = {
            "tar.bz2" : ""
        }
        BaseImageCreator.__init__(self, cfgmgr, pkgmgr)
        self._fstype = None
        self._fsopts = None
        self._include_src = False

    def package(self, destdir = "."):

        ignores = ["/dev/fd",
                   "/dev/stdin",
                   "/dev/stdout",
                   "/dev/stderr",
                   "/etc/mtab"]

        if not os.path.exists(destdir):
            os.makedirs(destdir)

        if self._recording_pkgs:
            self._save_recording_pkgs(destdir)

        if not self.pack_to:
            fsdir = os.path.join(destdir, self.name)

            misc.check_space_pre_cp(self._instroot, destdir)
            msger.info("Copying %s to %s ..." % (self._instroot, fsdir))
            runner.show(['cp', "-af", self._instroot, fsdir])

            for exclude in ignores:
                if os.path.exists(fsdir + exclude):
                    os.unlink(fsdir + exclude)

            self.outimage.append(fsdir)

        else:
            (tar, comp) = os.path.splitext(self.pack_to)
            try:
                tarcreat = {'.tar': '-cf',
                            '.gz': '-czf',
                            '.bz2': '-cjf',
                            '.tgz': '-czf',
                            '.tbz': '-cjf'}[comp]
            except KeyError:
                raise CreatorError("Unsupported comression for this image type:"
                                   " '%s', try '.tar', '.tar.gz', etc" % comp)

            dst = os.path.join(destdir, self.pack_to)
            msger.info("Pack rootfs to %s. Please wait..." % dst)

            tar = find_binary_path('tar')
            tar_cmdline = [tar, "--numeric-owner",
                                "--preserve-permissions",
                                "--preserve-order",
                                "--one-file-system",
                                "--directory",
                                self._instroot]
            for ignore_entry in ignores:
                if ignore_entry.startswith('/'):
                    ignore_entry = ignore_entry[1:]

                tar_cmdline.append("--exclude=%s" % (ignore_entry))

            tar_cmdline.extend([tarcreat, dst, "."])

            rc = runner.show(tar_cmdline)
            if rc:
                raise CreatorError("Failed compress image with tar.bz2. "
                                   "Cmdline: %s" % (" ".join(tar_cmdline)))

            self.outimage.append(dst)

