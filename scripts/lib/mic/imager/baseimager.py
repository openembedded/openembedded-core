#!/usr/bin/python -tt
#
# Copyright (c) 2007 Red Hat  Inc.
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
import os, sys
import tempfile
import shutil

from mic import kickstart
from mic import msger
from mic.utils.errors import CreatorError
from mic.utils import misc, runner, fs_related as fs

class BaseImageCreator(object):
    """Base class for image creation.

    BaseImageCreator is the simplest creator class available; it will
    create a system image according to the supplied kickstart file.

    e.g.

      import mic.imgcreate as imgcreate
      ks = imgcreate.read_kickstart("foo.ks")
      imgcreate.ImageCreator(ks, "foo").create()
    """

    def __del__(self):
        self.cleanup()

    def __init__(self, createopts = None):
        """Initialize an ImageCreator instance.

        ks -- a pykickstart.KickstartParser instance; this instance will be
              used to drive the install by e.g. providing the list of packages
              to be installed, the system configuration and %post scripts

        name -- a name for the image; used for e.g. image filenames or
                filesystem labels
        """

        self.__builddir = None

        self.ks = None
        self.name = "target"
        self.tmpdir = "/var/tmp/wic"
        self.workdir = "/var/tmp/wic/build"

        # setup tmpfs tmpdir when enabletmpfs is True
        self.enabletmpfs = False

        if createopts:
            # Mapping table for variables that have different names.
            optmap = {"outdir" : "destdir",
                     }

            # update setting from createopts
            for key in createopts.keys():
                if key in optmap:
                    option = optmap[key]
                else:
                    option = key
                setattr(self, option, createopts[key])

            self.destdir = os.path.abspath(os.path.expanduser(self.destdir))

        self._dep_checks = ["ls", "bash", "cp", "echo"]

        # Output image file names
        self.outimage = []

        # No ks provided when called by convertor, so skip the dependency check
        if self.ks:
            # If we have btrfs partition we need to check necessary tools
            for part in self.ks.handler.partition.partitions:
                if part.fstype and part.fstype == "btrfs":
                    self._dep_checks.append("mkfs.btrfs")
                    break

        # make sure the specified tmpdir and cachedir exist
        if not os.path.exists(self.tmpdir):
            os.makedirs(self.tmpdir)


    #
    # Properties
    #
    def __get_instroot(self):
        if self.__builddir is None:
            raise CreatorError("_instroot is not valid before calling mount()")
        return self.__builddir + "/install_root"
    _instroot = property(__get_instroot)
    """The location of the install root directory.

    This is the directory into which the system is installed. Subclasses may
    mount a filesystem image here or copy files to/from here.

    Note, this directory does not exist before ImageCreator.mount() is called.

    Note also, this is a read-only attribute.

    """


    #
    # Hooks for subclasses
    #
    def _mount_instroot(self, base_on = None):
        """Mount or prepare the install root directory.

        This is the hook where subclasses may prepare the install root by e.g.
        mounting creating and loopback mounting a filesystem image to
        _instroot.

        There is no default implementation.

        base_on -- this is the value passed to mount() and can be interpreted
                   as the subclass wishes; it might e.g. be the location of
                   a previously created ISO containing a system image.

        """
        pass

    def _unmount_instroot(self):
        """Undo anything performed in _mount_instroot().

        This is the hook where subclasses must undo anything which was done
        in _mount_instroot(). For example, if a filesystem image was mounted
        onto _instroot, it should be unmounted here.

        There is no default implementation.

        """
        pass

    #
    # Actual implementation
    #
    def __ensure_builddir(self):
        if not self.__builddir is None:
            return

        try:
            self.workdir = os.path.join(self.tmpdir, "build")
            if not os.path.exists(self.workdir):
                os.makedirs(self.workdir)
            self.__builddir = tempfile.mkdtemp(dir = self.workdir,
                                               prefix = "imgcreate-")
        except OSError, (err, msg):
            raise CreatorError("Failed create build directory in %s: %s" %
                               (self.tmpdir, msg))

    def __setup_tmpdir(self):
        if not self.enabletmpfs:
            return

        runner.show('mount -t tmpfs -o size=4G tmpfs %s' % self.workdir)

    def __clean_tmpdir(self):
        if not self.enabletmpfs:
            return

        runner.show('umount -l %s' % self.workdir)

    def mount(self, base_on = None, cachedir = None):
        """Setup the target filesystem in preparation for an install.

        This function sets up the filesystem which the ImageCreator will
        install into and configure. The ImageCreator class merely creates an
        install root directory, bind mounts some system directories (e.g. /dev)
        and writes out /etc/fstab. Other subclasses may also e.g. create a
        sparse file, format it and loopback mount it to the install root.

        base_on -- a previous install on which to base this install; defaults
                   to None, causing a new image to be created

        cachedir -- a directory in which to store the Yum cache; defaults to
                    None, causing a new cache to be created; by setting this
                    to another directory, the same cache can be reused across
                    multiple installs.

        """
        self.__setup_tmpdir()
        self.__ensure_builddir()

        self._mount_instroot(base_on)

    def unmount(self):
        """Unmounts the target filesystem.

        The ImageCreator class detaches the system from the install root, but
        other subclasses may also detach the loopback mounted filesystem image
        from the install root.

        """
        self._unmount_instroot()


    def cleanup(self):
        """Unmounts the target filesystem and deletes temporary files.

        This method calls unmount() and then deletes any temporary files and
        directories that were created on the host system while building the
        image.

        Note, make sure to call this method once finished with the creator
        instance in order to ensure no stale files are left on the host e.g.:

          creator = ImageCreator(ks, name)
          try:
              creator.create()
          finally:
              creator.cleanup()

        """
        if not self.__builddir:
            return

        self.unmount()

        shutil.rmtree(self.__builddir, ignore_errors = True)
        self.__builddir = None

        self.__clean_tmpdir()


    def print_outimage_info(self):
        msg = "The new image can be found here:\n"
        self.outimage.sort()
        for file in self.outimage:
            msg += '  %s\n' % os.path.abspath(file)

        msger.info(msg)
