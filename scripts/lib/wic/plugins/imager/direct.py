# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# Copyright (c) 2013, Intel Corporation.
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
# This implements the 'direct' imager plugin class for 'wic'
#
# AUTHORS
# Tom Zanussi <tom.zanussi (at] linux.intel.com>
#
import os
import shutil
import uuid
import tempfile

from time import strftime

from wic import msger
from wic.ksparser import KickStart, KickStartError
from wic.plugin import pluginmgr
from wic.pluginbase import ImagerPlugin
from wic.utils import errors
from wic.utils.errors import CreatorError, ImageError
from wic.utils.misc import get_bitbake_var, exec_cmd, exec_native_cmd
from wic.utils.partitionedfs import Image

class DiskImage():
    """
    A Disk backed by a file.
    """
    def __init__(self, device, size):
        self.size = size
        self.device = device
        self.created = False

    def create(self):
        if self.created:
            return
        # create sparse disk image
        with open(self.device, 'w') as sparse:
            os.ftruncate(sparse.fileno(), self.size)

        self.created = True

class DirectPlugin(ImagerPlugin):
    """
    Install a system into a file containing a partitioned disk image.

    An image file is formatted with a partition table, each partition
    created from a rootfs or other OpenEmbedded build artifact and dd'ed
    into the virtual disk. The disk image can subsequently be dd'ed onto
    media and used on actual hardware.
    """
    name = 'direct'

    def __init__(self, wks_file, rootfs_dir, bootimg_dir, kernel_dir,
                 native_sysroot, scripts_path, oe_builddir, options):
        try:
            self.ks = KickStart(wks_file)
        except KickStartError as err:
            msger.error(str(err))

        # parse possible 'rootfs=name' items
        self.rootfs_dir = dict(rdir.split('=') for rdir in rootfs_dir.split(' '))
        self.bootimg_dir = bootimg_dir
        self.kernel_dir = kernel_dir
        self.native_sysroot = native_sysroot
        self.oe_builddir = oe_builddir

        self.outdir = options.outdir
        self.compressor = options.compressor
        self.bmap = options.bmap

        self.name = "%s-%s" % (os.path.splitext(os.path.basename(wks_file))[0],
                               strftime("%Y%m%d%H%M"))
        self.workdir = tempfile.mkdtemp(dir=self.outdir, prefix='tmp.wic.')
        self._image = None
        self._disks = {}
        self._disk_format = "direct"
        self._disk_names = []
        self.ptable_format = self.ks.bootloader.ptable
        self.parts = self.ks.partitions

    def do_create(self):
        """
        Plugin entry point.
        """
        try:
            self.create()
            self.assemble()
            self.finalize()
            self.print_info()
        except errors.CreatorError:
            raise
        finally:
            self.cleanup()

    def _get_part_num(self, num, parts):
        """calculate the real partition number, accounting for partitions not
        in the partition table and logical partitions
        """
        realnum = 0
        for pnum, part in enumerate(parts, 1):
            if not part.no_table:
                realnum += 1
            if pnum == num:
                if  part.no_table:
                    return 0
                if self.ptable_format == 'msdos' and realnum > 3:
                    # account for logical partition numbering, ex. sda5..
                    return realnum + 1
                return realnum

    def _write_fstab(self, image_rootfs):
        """overriden to generate fstab (temporarily) in rootfs. This is called
        from _create, make sure it doesn't get called from
        BaseImage.create()
        """
        if not image_rootfs:
            return

        fstab_path = image_rootfs + "/etc/fstab"
        if not os.path.isfile(fstab_path):
            return

        with open(fstab_path) as fstab:
            fstab_lines = fstab.readlines()

        if self._update_fstab(fstab_lines, self.parts):
            shutil.copyfile(fstab_path, fstab_path + ".orig")

            with open(fstab_path, "w") as fstab:
                fstab.writelines(fstab_lines)

            return fstab_path

    def _update_fstab(self, fstab_lines, parts):
        """Assume partition order same as in wks"""
        updated = False
        for num, part in enumerate(parts, 1):
            pnum = self._get_part_num(num, parts)
            if not pnum or not part.mountpoint \
               or part.mountpoint in ("/", "/boot"):
                continue

            # mmc device partitions are named mmcblk0p1, mmcblk0p2..
            prefix = 'p' if  part.disk.startswith('mmcblk') else ''
            device_name = "/dev/%s%s%d" % (part.disk, prefix, pnum)

            opts = part.fsopts if part.fsopts else "defaults"
            line = "\t".join([device_name, part.mountpoint, part.fstype,
                              opts, "0", "0"]) + "\n"

            fstab_lines.append(line)
            updated = True

        return updated

    def set_bootimg_dir(self, bootimg_dir):
        """
        Accessor for bootimg_dir, the actual location used for the source
        of the bootimg.  Should be set by source plugins (only if they
        change the default bootimg source) so the correct info gets
        displayed for print_outimage_info().
        """
        self.bootimg_dir = bootimg_dir

    def _full_path(self, path, name, extention):
        """ Construct full file path to a file we generate. """
        return os.path.join(path, "%s-%s.%s" % (self.name, name, extention))

    #
    # Actual implemention
    #
    def create(self):
        """
        For 'wic', we already have our build artifacts - we just create
        filesystems from the artifacts directly and combine them into
        a partitioned image.
        """
        self._image = Image(self.native_sysroot)

        disk_ids = {}
        for num, part in enumerate(self.parts, 1):
            # as a convenience, set source to the boot partition source
            # instead of forcing it to be set via bootloader --source
            if not self.ks.bootloader.source and part.mountpoint == "/boot":
                self.ks.bootloader.source = part.source

            # generate parition UUIDs
            if not part.uuid and part.use_uuid:
                if self.ptable_format == 'gpt':
                    part.uuid = str(uuid.uuid4())
                else: # msdos partition table
                    if part.disk not in disk_ids:
                        disk_ids[part.disk] = int.from_bytes(os.urandom(4), 'little')
                    disk_id = disk_ids[part.disk]
                    part.uuid = '%0x-%02d' % (disk_id, self._get_part_num(num, self.parts))

        fstab_path = self._write_fstab(self.rootfs_dir.get("ROOTFS_DIR"))

        for part in self.parts:
            # get rootfs size from bitbake variable if it's not set in .ks file
            if not part.size:
                # and if rootfs name is specified for the partition
                image_name = self.rootfs_dir.get(part.rootfs_dir)
                if image_name and os.path.sep not in image_name:
                    # Bitbake variable ROOTFS_SIZE is calculated in
                    # Image._get_rootfs_size method from meta/lib/oe/image.py
                    # using IMAGE_ROOTFS_SIZE, IMAGE_ROOTFS_ALIGNMENT,
                    # IMAGE_OVERHEAD_FACTOR and IMAGE_ROOTFS_EXTRA_SPACE
                    rsize_bb = get_bitbake_var('ROOTFS_SIZE', image_name)
                    if rsize_bb:
                        part.size = int(round(float(rsize_bb)))
            # need to create the filesystems in order to get their
            # sizes before we can add them and do the layout.
            # Image.create() actually calls __format_disks() to create
            # the disk images and carve out the partitions, then
            # self.assemble() calls Image.assemble() which calls
            # __write_partitition() for each partition to dd the fs
            # into the partitions.
            part.prepare(self, self.workdir, self.oe_builddir, self.rootfs_dir,
                         self.bootimg_dir, self.kernel_dir, self.native_sysroot)


            self._image.add_partition(part.disk_size, part.disk,
                                      part.mountpoint, part.source_file,
                                      part.fstype, part.label,
                                      fsopts=part.fsopts, boot=part.active,
                                      align=part.align, no_table=part.no_table,
                                      part_type=part.part_type, uuid=part.uuid,
                                      system_id=part.system_id)

        if fstab_path:
            shutil.move(fstab_path + ".orig", fstab_path)

        self._image.layout_partitions(self.ptable_format)

        for disk_name, disk in self._image.disks.items():
            full_path = self._full_path(self.workdir, disk_name, "direct")
            msger.debug("Adding disk %s as %s with size %s bytes" \
                        % (disk_name, full_path, disk['min_size']))
            disk_obj = DiskImage(full_path, disk['min_size'])
            #self._disks[disk_name] = disk_obj
            self._image.add_disk(disk_name, disk_obj, disk_ids.get(disk_name))

        self._image.create()

    def assemble(self):
        """
        Assemble partitions into disk image(s)
        """
        for disk_name, disk in self._image.disks.items():
            full_path = self._full_path(self.workdir, disk_name, "direct")
            msger.debug("Assembling disk %s as %s with size %s bytes" \
                        % (disk_name, full_path, disk['min_size']))
            self._image.assemble(full_path)

    def finalize(self):
        """
        Finalize the disk image.

        For example, prepare the image to be bootable by e.g.
        creating and installing a bootloader configuration.
        """
        source_plugin = self.ks.bootloader.source
        if source_plugin:
            name = "do_install_disk"
            methods = pluginmgr.get_source_plugin_methods(source_plugin,
                                                          {name: None})
            for disk_name, disk in self._image.disks.items():
                methods["do_install_disk"](disk, disk_name, self, self.workdir,
                                           self.oe_builddir, self.bootimg_dir,
                                           self.kernel_dir, self.native_sysroot)

        for disk_name, disk in self._image.disks.items():
            full_path = self._full_path(self.workdir, disk_name, "direct")
            # Generate .bmap
            if self.bmap:
                msger.debug("Generating bmap file for %s" % disk_name)
                exec_native_cmd("bmaptool create %s -o %s.bmap" % (full_path, full_path),
                                self.native_sysroot)
            # Compress the image
            if self.compressor:
                msger.debug("Compressing disk %s with %s" % (disk_name, self.compressor))
                exec_cmd("%s %s" % (self.compressor, full_path))

    def print_info(self):
        """
        Print the image(s) and artifacts used, for the user.
        """
        msg = "The new image(s) can be found here:\n"

        for disk_name in self._image.disks:
            extension = "direct" + {"gzip": ".gz",
                                    "bzip2": ".bz2",
                                    "xz": ".xz",
                                    None: ""}.get(self.compressor)
            full_path = self._full_path(self.outdir, disk_name, extension)
            msg += '  %s\n\n' % full_path

        msg += 'The following build artifacts were used to create the image(s):\n'
        for part in self.parts:
            if part.rootfs_dir is None:
                continue
            if part.mountpoint == '/':
                suffix = ':'
            else:
                suffix = '["%s"]:' % (part.mountpoint or part.label)
            msg += '  ROOTFS_DIR%s%s\n' % (suffix.ljust(20), part.rootfs_dir)

        msg += '  BOOTIMG_DIR:                  %s\n' % self.bootimg_dir
        msg += '  KERNEL_DIR:                   %s\n' % self.kernel_dir
        msg += '  NATIVE_SYSROOT:               %s\n' % self.native_sysroot

        msger.info(msg)

    @property
    def rootdev(self):
        """
        Get root device name to use as a 'root' parameter
        in kernel command line.

        Assume partition order same as in wks
        """
        for num, part in enumerate(self.parts, 1):
            if part.mountpoint == "/":
                if part.uuid:
                    return "PARTUUID=%s" % part.uuid
                else:
                    suffix = 'p' if part.disk.startswith('mmcblk') else ''
                    pnum = self._get_part_num(num, self.parts)
                    return "/dev/%s%s%-d" % (part.disk, suffix, pnum)

    def cleanup(self):
        if self._image:
            try:
                self._image.cleanup()
            except ImageError as err:
                msger.warning("%s" % err)

        # Move results to the output dir
        if not os.path.exists(self.outdir):
            os.makedirs(self.outdir)

        for fname in os.listdir(self.workdir):
            path = os.path.join(self.workdir, fname)
            if os.path.isfile(path):
                shutil.move(path, os.path.join(self.outdir, fname))

        # remove work directory
        shutil.rmtree(self.workdir, ignore_errors=True)
