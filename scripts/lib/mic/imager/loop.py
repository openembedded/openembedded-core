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
import glob
import shutil

from mic import kickstart, msger
from mic.utils.errors import CreatorError, MountError
from mic.utils import misc, runner, fs_related as fs
from mic.imager.baseimager import BaseImageCreator


# The maximum string length supported for LoopImageCreator.fslabel
FSLABEL_MAXLEN = 32


def save_mountpoints(fpath, loops, arch = None):
    """Save mount points mapping to file

    :fpath, the xml file to store partition info
    :loops, dict of partition info
    :arch, image arch
    """

    if not fpath or not loops:
        return

    from xml.dom import minidom
    doc = minidom.Document()
    imgroot = doc.createElement("image")
    doc.appendChild(imgroot)
    if arch:
        imgroot.setAttribute('arch', arch)
    for loop in loops:
        part = doc.createElement("partition")
        imgroot.appendChild(part)
        for (key, val) in loop.items():
            if isinstance(val, fs.Mount):
                continue
            part.setAttribute(key, str(val))

    with open(fpath, 'w') as wf:
        wf.write(doc.toprettyxml(indent='  '))

    return

def load_mountpoints(fpath):
    """Load mount points mapping from file

    :fpath, file path to load
    """

    if not fpath:
        return

    from xml.dom import minidom
    mount_maps = []
    with open(fpath, 'r') as rf:
        dom = minidom.parse(rf)
    imgroot = dom.documentElement
    for part in imgroot.getElementsByTagName("partition"):
        p  = dict(part.attributes.items())

        try:
            mp = (p['mountpoint'], p['label'], p['name'],
                  int(p['size']), p['fstype'])
        except KeyError:
            msger.warning("Wrong format line in file: %s" % fpath)
        except ValueError:
            msger.warning("Invalid size '%s' in file: %s" % (p['size'], fpath))
        else:
            mount_maps.append(mp)

    return mount_maps

class LoopImageCreator(BaseImageCreator):
    """Installs a system into a loopback-mountable filesystem image.

    LoopImageCreator is a straightforward ImageCreator subclass; the system
    is installed into an ext3 filesystem on a sparse file which can be
    subsequently loopback-mounted.

    When specifying multiple partitions in kickstart file, each partition
    will be created as a separated loop image.
    """

    def __init__(self, creatoropts=None, pkgmgr=None,
                 compress_image=None,
                 shrink_image=False):
        """Initialize a LoopImageCreator instance.

        This method takes the same arguments as ImageCreator.__init__()
        with the addition of:

        fslabel -- A string used as a label for any filesystems created.
        """

        BaseImageCreator.__init__(self, creatoropts, pkgmgr)

        self.compress_image = compress_image
        self.shrink_image = shrink_image

        self.__fslabel = None
        self.fslabel = self.name

        self.__blocksize = 4096
        if self.ks:
            self.__fstype = kickstart.get_image_fstype(self.ks,
                                                       "ext3")
            self.__fsopts = kickstart.get_image_fsopts(self.ks,
                                                       "defaults,noatime")

            allloops = []
            for part in sorted(kickstart.get_partitions(self.ks),
                               key=lambda p: p.mountpoint):
                if part.fstype == "swap":
                    continue

                label = part.label
                mp = part.mountpoint
                if mp == '/':
                    # the base image
                    if not label:
                        label = self.name
                else:
                    mp = mp.rstrip('/')
                    if not label:
                        msger.warning('no "label" specified for loop img at %s'
                                      ', use the mountpoint as the name' % mp)
                        label = mp.split('/')[-1]

                imgname = misc.strip_end(label, '.img') + '.img'
                allloops.append({
                    'mountpoint': mp,
                    'label': label,
                    'name': imgname,
                    'size': part.size or 4096L * 1024 * 1024,
                    'fstype': part.fstype or 'ext3',
                    'extopts': part.extopts or None,
                    'loop': None,  # to be created in _mount_instroot
                    })
            self._instloops = allloops

        else:
            self.__fstype = None
            self.__fsopts = None
            self._instloops = []

        self.__imgdir = None

        if self.ks:
            self.__image_size = kickstart.get_image_size(self.ks,
                                                         4096L * 1024 * 1024)
        else:
            self.__image_size = 0

        self._img_name = self.name + ".img"

    def get_image_names(self):
        if not self._instloops:
            return None

        return [lo['name'] for lo in self._instloops]

    def _set_fstype(self, fstype):
        self.__fstype = fstype

    def _set_image_size(self, imgsize):
        self.__image_size = imgsize


    #
    # Properties
    #
    def __get_fslabel(self):
        if self.__fslabel is None:
            return self.name
        else:
            return self.__fslabel
    def __set_fslabel(self, val):
        if val is None:
            self.__fslabel = None
        else:
            self.__fslabel = val[:FSLABEL_MAXLEN]
    #A string used to label any filesystems created.
    #
    #Some filesystems impose a constraint on the maximum allowed size of the
    #filesystem label. In the case of ext3 it's 16 characters, but in the case
    #of ISO9660 it's 32 characters.
    #
    #mke2fs silently truncates the label, but mkisofs aborts if the label is
    #too long. So, for convenience sake, any string assigned to this attribute
    #is silently truncated to FSLABEL_MAXLEN (32) characters.
    fslabel = property(__get_fslabel, __set_fslabel)

    def __get_image(self):
        if self.__imgdir is None:
            raise CreatorError("_image is not valid before calling mount()")
        return os.path.join(self.__imgdir, self._img_name)
    #The location of the image file.
    #
    #This is the path to the filesystem image. Subclasses may use this path
    #in order to package the image in _stage_final_image().
    #
    #Note, this directory does not exist before ImageCreator.mount() is called.
    #
    #Note also, this is a read-only attribute.
    _image = property(__get_image)

    def __get_blocksize(self):
        return self.__blocksize
    def __set_blocksize(self, val):
        if self._instloops:
            raise CreatorError("_blocksize must be set before calling mount()")
        try:
            self.__blocksize = int(val)
        except ValueError:
            raise CreatorError("'%s' is not a valid integer value "
                               "for _blocksize" % val)
    #The block size used by the image's filesystem.
    #
    #This is the block size used when creating the filesystem image. Subclasses
    #may change this if they wish to use something other than a 4k block size.
    #
    #Note, this attribute may only be set before calling mount().
    _blocksize = property(__get_blocksize, __set_blocksize)

    def __get_fstype(self):
        return self.__fstype
    def __set_fstype(self, val):
        if val != "ext2" and val != "ext3":
            raise CreatorError("Unknown _fstype '%s' supplied" % val)
        self.__fstype = val
    #The type of filesystem used for the image.
    #
    #This is the filesystem type used when creating the filesystem image.
    #Subclasses may change this if they wish to use something other ext3.
    #
    #Note, only ext2 and ext3 are currently supported.
    #
    #Note also, this attribute may only be set before calling mount().
    _fstype = property(__get_fstype, __set_fstype)

    def __get_fsopts(self):
        return self.__fsopts
    def __set_fsopts(self, val):
        self.__fsopts = val
    #Mount options of filesystem used for the image.
    #
    #This can be specified by --fsoptions="xxx,yyy" in part command in
    #kickstart file.
    _fsopts = property(__get_fsopts, __set_fsopts)


    #
    # Helpers for subclasses
    #
    def _resparse(self, size=None):
        """Rebuild the filesystem image to be as sparse as possible.

        This method should be used by subclasses when staging the final image
        in order to reduce the actual space taken up by the sparse image file
        to be as little as possible.

        This is done by resizing the filesystem to the minimal size (thereby
        eliminating any space taken up by deleted files) and then resizing it
        back to the supplied size.

        size -- the size in, in bytes, which the filesystem image should be
                resized to after it has been minimized; this defaults to None,
                causing the original size specified by the kickstart file to
                be used (or 4GiB if not specified in the kickstart).
        """
        minsize = 0
        for item in self._instloops:
            if item['name'] == self._img_name:
                minsize = item['loop'].resparse(size)
            else:
                item['loop'].resparse(size)

        return minsize

    def _base_on(self, base_on=None):
        if base_on and self._image != base_on:
            shutil.copyfile(base_on, self._image)

    def _check_imgdir(self):
        if self.__imgdir is None:
            self.__imgdir = self._mkdtemp()


    #
    # Actual implementation
    #
    def _mount_instroot(self, base_on=None):

        if base_on and os.path.isfile(base_on):
            self.__imgdir = os.path.dirname(base_on)
            imgname = os.path.basename(base_on)
            self._base_on(base_on)
            self._set_image_size(misc.get_file_size(self._image))

            # here, self._instloops must be []
            self._instloops.append({
                 "mountpoint": "/",
                 "label": self.name,
                 "name": imgname,
                 "size": self.__image_size or 4096L,
                 "fstype": self.__fstype or "ext3",
                 "extopts": None,
                 "loop": None
                 })

        self._check_imgdir()

        for loop in self._instloops:
            fstype = loop['fstype']
            mp = os.path.join(self._instroot, loop['mountpoint'].lstrip('/'))
            size = loop['size'] * 1024L * 1024L
            imgname = loop['name']

            if fstype in ("ext2", "ext3", "ext4"):
                MyDiskMount = fs.ExtDiskMount
            elif fstype == "btrfs":
                MyDiskMount = fs.BtrfsDiskMount
            elif fstype in ("vfat", "msdos"):
                MyDiskMount = fs.VfatDiskMount
            else:
                msger.error('Cannot support fstype: %s' % fstype)

            loop['loop'] = MyDiskMount(fs.SparseLoopbackDisk(
                                           os.path.join(self.__imgdir, imgname),
                                           size),
                                       mp,
                                       fstype,
                                       self._blocksize,
                                       loop['label'])

            if fstype in ("ext2", "ext3", "ext4"):
                loop['loop'].extopts = loop['extopts']

            try:
                msger.verbose('Mounting image "%s" on "%s"' % (imgname, mp))
                fs.makedirs(mp)
                loop['loop'].mount()
            except MountError, e:
                raise

    def _unmount_instroot(self):
        for item in reversed(self._instloops):
            try:
                item['loop'].cleanup()
            except:
                pass

    def _stage_final_image(self):

        if self.pack_to or self.shrink_image:
            self._resparse(0)
        else:
            self._resparse()

        for item in self._instloops:
            imgfile = os.path.join(self.__imgdir, item['name'])
            if item['fstype'] == "ext4":
                runner.show('/sbin/tune2fs -O ^huge_file,extents,uninit_bg %s '
                            % imgfile)
            if self.compress_image:
                misc.compressing(imgfile, self.compress_image)

        if not self.pack_to:
            for item in os.listdir(self.__imgdir):
                shutil.move(os.path.join(self.__imgdir, item),
                            os.path.join(self._outdir, item))
        else:
            msger.info("Pack all loop images together to %s" % self.pack_to)
            dstfile = os.path.join(self._outdir, self.pack_to)
            misc.packing(dstfile, self.__imgdir)

        if self.pack_to:
            mountfp_xml = os.path.splitext(self.pack_to)[0]
            mountfp_xml = misc.strip_end(mountfp_xml, '.tar') + ".xml"
        else:
            mountfp_xml = self.name + ".xml"
        # save mount points mapping file to xml
        save_mountpoints(os.path.join(self._outdir, mountfp_xml),
                         self._instloops,
                         self.target_arch)

    def copy_attachment(self):
        if not hasattr(self, '_attachment') or not self._attachment:
            return

        self._check_imgdir()

        msger.info("Copying attachment files...")
        for item in self._attachment:
            if not os.path.exists(item):
                continue
            dpath = os.path.join(self.__imgdir, os.path.basename(item))
            msger.verbose("Copy attachment %s to %s" % (item, dpath))
            shutil.copy(item, dpath)

