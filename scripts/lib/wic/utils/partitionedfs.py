#!/usr/bin/env python -tt
#
# Copyright (c) 2009, 2010, 2011 Intel, Inc.
# Copyright (c) 2007, 2008 Red Hat, Inc.
# Copyright (c) 2008 Daniel P. Berrange
# Copyright (c) 2008 David P. Huff
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

from wic import msger
from wic.utils import runner
from wic.utils.errors import ImageError
from wic.utils.fs_related import *
from wic.utils.oe.misc import *

# Overhead of the MBR partitioning scheme (just one sector)
MBR_OVERHEAD = 1

# Size of a sector in bytes
SECTOR_SIZE = 512

class Image:
    """
    Generic base object for an image.

    An Image is a container for a set of DiskImages and associated
    partitions.
    """
    def __init__(self):
        self.disks = {}
        self.partitions = []
        self.parted = find_binary_path("parted")
        # Size of a sector used in calculations
        self.sector_size = SECTOR_SIZE
        self._partitions_layed_out = False

    def __add_disk(self, disk_name):
        """ Add a disk 'disk_name' to the internal list of disks. Note,
        'disk_name' is the name of the disk in the target system
        (e.g., sdb). """

        if disk_name in self.disks:
            # We already have this disk
            return

        assert not self._partitions_layed_out

        self.disks[disk_name] = \
                { 'disk': None,     # Disk object
                  'numpart': 0,     # Number of allocate partitions
                  'partitions': [], # Indexes to self.partitions
                  'offset': 0,      # Offset of next partition (in sectors)
                  # Minimum required disk size to fit all partitions (in bytes)
                  'min_size': 0,
                  'ptable_format': "msdos" } # Partition table format

    def add_disk(self, disk_name, disk_obj):
        """ Add a disk object which have to be partitioned. More than one disk
        can be added. In case of multiple disks, disk partitions have to be
        added for each disk separately with 'add_partition()". """

        self.__add_disk(disk_name)
        self.disks[disk_name]['disk'] = disk_obj

    def __add_partition(self, part):
        """ This is a helper function for 'add_partition()' which adds a
        partition to the internal list of partitions. """

        assert not self._partitions_layed_out

        self.partitions.append(part)
        self.__add_disk(part['disk_name'])

    def add_partition(self, size, disk_name, mountpoint, source_file = None, fstype = None,
                      label=None, fsopts = None, boot = False, align = None,
                      part_type = None):
        """ Add the next partition. Prtitions have to be added in the
        first-to-last order. """

        ks_pnum = len(self.partitions)

        # Converting MB to sectors for parted
        size = size * 1024 * 1024 / self.sector_size

        # We still need partition for "/" or non-subvolume
        if mountpoint == "/" or not fsopts:
            part = { 'ks_pnum' : ks_pnum, # Partition number in the KS file
                     'size': size, # In sectors
                     'mountpoint': mountpoint, # Mount relative to chroot
                     'source_file': source_file, # partition contents
                     'fstype': fstype, # Filesystem type
                     'fsopts': fsopts, # Filesystem mount options
                     'label': label, # Partition label
                     'disk_name': disk_name, # physical disk name holding partition
                     'device': None, # kpartx device node for partition
                     'num': None, # Partition number
                     'boot': boot, # Bootable flag
                     'align': align, # Partition alignment
                     'part_type' : part_type } # Partition type

            self.__add_partition(part)

    def layout_partitions(self, ptable_format = "msdos"):
        """ Layout the partitions, meaning calculate the position of every
        partition on the disk. The 'ptable_format' parameter defines the
        partition table format and may be "msdos". """

        msger.debug("Assigning %s partitions to disks" % ptable_format)

        if ptable_format not in ('msdos'):
            raise ImageError("Unknown partition table format '%s', supported " \
                             "formats are: 'msdos'" % ptable_format)

        if self._partitions_layed_out:
            return

        self._partitions_layed_out = True

        # Go through partitions in the order they are added in .ks file
        for n in range(len(self.partitions)):
            p = self.partitions[n]

            if not self.disks.has_key(p['disk_name']):
                raise ImageError("No disk %s for partition %s" \
                                 % (p['disk_name'], p['mountpoint']))

            if p['part_type']:
                # The --part-type can also be implemented for MBR partitions,
                # in which case it would map to the 1-byte "partition type"
                # filed at offset 3 of the partition entry.
                raise ImageError("setting custom partition type is not " \
                                 "implemented for msdos partitions")

            # Get the disk where the partition is located
            d = self.disks[p['disk_name']]
            d['numpart'] += 1
            d['ptable_format'] = ptable_format

            if d['numpart'] == 1:
                if ptable_format == "msdos":
                    overhead = MBR_OVERHEAD

                # Skip one sector required for the partitioning scheme overhead
                d['offset'] += overhead

            if p['align']:
                # If not first partition and we do have alignment set we need
                # to align the partition.
                # FIXME: This leaves a empty spaces to the disk. To fill the
                # gaps we could enlargea the previous partition?

                # Calc how much the alignment is off.
                align_sectors = d['offset'] % (p['align'] * 1024 / self.sector_size)

                if align_sectors:
                    # If partition is not aligned as required, we need
                    # to move forward to the next alignment point
                    align_sectors = (p['align'] * 1024 / self.sector_size) - align_sectors

                    msger.debug("Realignment for %s%s with %s sectors, original"
                                " offset %s, target alignment is %sK." %
                                (p['disk_name'], d['numpart'], align_sectors,
                                 d['offset'], p['align']))

                    # increase the offset so we actually start the partition on right alignment
                    d['offset'] += align_sectors

            p['start'] = d['offset']
            d['offset'] += p['size']

            p['type'] = 'primary'
            p['num'] = d['numpart']

            if d['ptable_format'] == "msdos":
                if d['numpart'] > 2:
                    # Every logical partition requires an additional sector for
                    # the EBR, so steal the last sector from the end of each
                    # partition starting from the 3rd one for the EBR. This
                    # will make sure the logical partitions are aligned
                    # correctly.
                    p['size'] -= 1

                if d['numpart'] > 3:
                    p['type'] = 'logical'
                    p['num'] = d['numpart'] + 1

            d['partitions'].append(n)
            msger.debug("Assigned %s to %s%d, sectors range %d-%d size %d "
                        "sectors (%d bytes)." \
                            % (p['mountpoint'], p['disk_name'], p['num'],
                               p['start'], p['start'] + p['size'] - 1,
                               p['size'], p['size'] * self.sector_size))

        # Once all the partitions have been layed out, we can calculate the
        # minumim disk sizes.
        for disk_name, d in self.disks.items():
            d['min_size'] = d['offset']

            d['min_size'] *= self.sector_size

    def __run_parted(self, args):
        """ Run parted with arguments specified in the 'args' list. """

        args.insert(0, self.parted)
        msger.debug(args)

        rc, out = runner.runtool(args, catch = 3)
        out = out.strip()
        if out:
            msger.debug('"parted" output: %s' % out)

        if rc != 0:
            # We don't throw exception when return code is not 0, because
            # parted always fails to reload part table with loop devices. This
            # prevents us from distinguishing real errors based on return
            # code.
            msger.error("WARNING: parted returned '%s' instead of 0 (use --debug for details)" % rc)

    def __create_partition(self, device, parttype, fstype, start, size):
        """ Create a partition on an image described by the 'device' object. """

        # Start is included to the size so we need to substract one from the end.
        end = start + size - 1
        msger.debug("Added '%s' partition, sectors %d-%d, size %d sectors" %
                    (parttype, start, end, size))

        args = ["-s", device, "unit", "s", "mkpart", parttype]
        if fstype:
            args.extend([fstype])
        args.extend(["%d" % start, "%d" % end])

        return self.__run_parted(args)

    def __format_disks(self):
        self.layout_partitions()

        for dev in self.disks.keys():
            d = self.disks[dev]
            msger.debug("Initializing partition table for %s" % \
                        (d['disk'].device))
            self.__run_parted(["-s", d['disk'].device, "mklabel",
                               d['ptable_format']])

        msger.debug("Creating partitions")

        for p in self.partitions:
            d = self.disks[p['disk_name']]
            if d['ptable_format'] == "msdos" and p['num'] == 5:
                # The last sector of the 3rd partition was reserved for the EBR
                # of the first _logical_ partition. This is why the extended
                # partition should start one sector before the first logical
                # partition.
                self.__create_partition(d['disk'].device, "extended",
                                        None, p['start'] - 1,
                                        d['offset'] - p['start'])

            if p['fstype'] == "swap":
                parted_fs_type = "linux-swap"
            elif p['fstype'] == "vfat":
                parted_fs_type = "fat32"
            elif p['fstype'] == "msdos":
                parted_fs_type = "fat16"
            else:
                # Type for ext2/ext3/ext4/btrfs
                parted_fs_type = "ext2"

            # Boot ROM of OMAP boards require vfat boot partition to have an
            # even number of sectors.
            if p['mountpoint'] == "/boot" and p['fstype'] in ["vfat", "msdos"] \
               and p['size'] % 2:
                msger.debug("Substracting one sector from '%s' partition to " \
                            "get even number of sectors for the partition" % \
                            p['mountpoint'])
                p['size'] -= 1

            self.__create_partition(d['disk'].device, p['type'],
                                    parted_fs_type, p['start'], p['size'])

            if p['boot']:
                flag_name = "boot"
                msger.debug("Set '%s' flag for partition '%s' on disk '%s'" % \
                            (flag_name, p['num'], d['disk'].device))
                self.__run_parted(["-s", d['disk'].device, "set",
                                   "%d" % p['num'], flag_name, "on"])

            # Parted defaults to enabling the lba flag for fat16 partitions,
            # which causes compatibility issues with some firmware (and really
            # isn't necessary).
            if parted_fs_type == "fat16":
                if d['ptable_format'] == 'msdos':
                    msger.debug("Disable 'lba' flag for partition '%s' on disk '%s'" % \
                                (p['num'], d['disk'].device))
                    self.__run_parted(["-s", d['disk'].device, "set",
                                       "%d" % p['num'], "lba", "off"])

    def cleanup(self):
        if self.disks:
            for dev in self.disks.keys():
                d = self.disks[dev]
                try:
                    d['disk'].cleanup()
                except:
                    pass

    def __write_partition(self, num, source_file, start, size):
        """
        Install source_file contents into a partition.
        """
        if not source_file: # nothing to write
            return

        # Start is included in the size so need to substract one from the end.
        end = start + size - 1
        msger.debug("Installed %s in partition %d, sectors %d-%d, size %d sectors" % (source_file, num, start, end, size))

        dd_cmd = "dd if=%s of=%s bs=%d seek=%d count=%d conv=notrunc" % \
            (source_file, self.image_file, self.sector_size, start, size)
        exec_cmd(dd_cmd)


    def assemble(self, image_file):
        msger.debug("Installing partitions")

        self.image_file = image_file

        for p in self.partitions:
            d = self.disks[p['disk_name']]
            if d['ptable_format'] == "msdos" and p['num'] == 5:
                # The last sector of the 3rd partition was reserved for the EBR
                # of the first _logical_ partition. This is why the extended
                # partition should start one sector before the first logical
                # partition.
                self.__write_partition(p['num'], p['source_file'],
                                       p['start'] - 1,
                                       d['offset'] - p['start'])

            self.__write_partition(p['num'], p['source_file'],
                                   p['start'], p['size'])

    def create(self):
        for dev in self.disks.keys():
            d = self.disks[dev]
            d['disk'].create()

        self.__format_disks()

        return
