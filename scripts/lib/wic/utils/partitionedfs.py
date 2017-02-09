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
from wic.utils.errors import ImageError
from wic.utils.misc import exec_native_cmd
from wic.filemap import sparse_copy

# Overhead of the MBR partitioning scheme (just one sector)
MBR_OVERHEAD = 1

# Overhead of the GPT partitioning scheme
GPT_OVERHEAD = 34

# Size of a sector in bytes
SECTOR_SIZE = 512

class PartitionedImage():
    """
    Partitioned image in a file.
    """

    def __init__(self, path, ptable_format, native_sysroot=None):
        self.path = path  # Path to the image file
        self.numpart = 0  # Number of allocated partitions
        self.realpart = 0 # Number of partitions in the partition table
        self.offset = 0   # Offset of next partition (in sectors)
        self.min_size = 0 # Minimum required disk size to fit
                          # all partitions (in bytes)
        self.ptable_format = ptable_format  # Partition table format
        # Disk system identifier
        self.identifier = int.from_bytes(os.urandom(4), 'little')

        self.partitions = []
        self.partimages = []
        # Size of a sector used in calculations
        self.sector_size = SECTOR_SIZE
        self.native_sysroot = native_sysroot

    def add_partition(self, part):
        """
        Add the next partition. Partitions have to be added in the
        first-to-last order.
        """
        part.ks_pnum = len(self.partitions)

        # Converting kB to sectors for parted
        part.size_sec = part.disk_size * 1024 // self.sector_size

        self.partitions.append(part)

    def layout_partitions(self):
        """ Layout the partitions, meaning calculate the position of every
        partition on the disk. The 'ptable_format' parameter defines the
        partition table format and may be "msdos". """

        msger.debug("Assigning %s partitions to disks" % self.ptable_format)

        # Go through partitions in the order they are added in .ks file
        for num in range(len(self.partitions)):
            part = self.partitions[num]

            if self.ptable_format == 'msdos' and part.part_type:
                # The --part-type can also be implemented for MBR partitions,
                # in which case it would map to the 1-byte "partition type"
                # filed at offset 3 of the partition entry.
                raise ImageError("setting custom partition type is not " \
                                 "implemented for msdos partitions")

            # Get the disk where the partition is located
            self.numpart += 1
            if not part.no_table:
                self.realpart += 1

            if self.numpart == 1:
                if self.ptable_format == "msdos":
                    overhead = MBR_OVERHEAD
                elif self.ptable_format == "gpt":
                    overhead = GPT_OVERHEAD

                # Skip one sector required for the partitioning scheme overhead
                self.offset += overhead

            if self.realpart > 3:
                # Reserve a sector for EBR for every logical partition
                # before alignment is performed.
                if self.ptable_format == "msdos":
                    self.offset += 1

            if part.align:
                # If not first partition and we do have alignment set we need
                # to align the partition.
                # FIXME: This leaves a empty spaces to the disk. To fill the
                # gaps we could enlargea the previous partition?

                # Calc how much the alignment is off.
                align_sectors = self.offset % (part.align * 1024 // self.sector_size)

                if align_sectors:
                    # If partition is not aligned as required, we need
                    # to move forward to the next alignment point
                    align_sectors = (part.align * 1024 // self.sector_size) - align_sectors

                    msger.debug("Realignment for %s%s with %s sectors, original"
                                " offset %s, target alignment is %sK." %
                                (part.disk, self.numpart, align_sectors,
                                 self.offset, part.align))

                    # increase the offset so we actually start the partition on right alignment
                    self.offset += align_sectors

            part.start = self.offset
            self.offset += part.size_sec

            part.type = 'primary'
            if not part.no_table:
                part.num = self.realpart
            else:
                part.num = 0

            if self.ptable_format == "msdos":
                # only count the partitions that are in partition table
                if len([p for p in self.partitions if not p.no_table]) > 4:
                    if self.realpart > 3:
                        part.type = 'logical'
                        part.num = self.realpart + 1

            msger.debug("Assigned %s to %s%d, sectors range %d-%d size %d "
                        "sectors (%d bytes)." \
                            % (part.mountpoint, part.disk, part.num,
                               part.start, self.offset - 1,
                               part.size_sec, part.size_sec * self.sector_size))

        # Once all the partitions have been layed out, we can calculate the
        # minumim disk size
        self.min_size = self.offset
        if self.ptable_format == "gpt":
            self.min_size += GPT_OVERHEAD

        self.min_size *= self.sector_size

    def _create_partition(self, device, parttype, fstype, start, size):
        """ Create a partition on an image described by the 'device' object. """

        # Start is included to the size so we need to substract one from the end.
        end = start + size - 1
        msger.debug("Added '%s' partition, sectors %d-%d, size %d sectors" %
                    (parttype, start, end, size))

        cmd = "parted -s %s unit s mkpart %s" % (device, parttype)
        if fstype:
            cmd += " %s" % fstype
        cmd += " %d %d" % (start, end)

        return exec_native_cmd(cmd, self.native_sysroot)

    def create(self):
        msger.debug("Creating sparse file %s" % self.path)
        with open(self.path, 'w') as sparse:
            os.ftruncate(sparse.fileno(), self.min_size)

        msger.debug("Initializing partition table for %s" % self.path)
        exec_native_cmd("parted -s %s mklabel %s" %
                        (self.path, self.ptable_format), self.native_sysroot)

        msger.debug("Set disk identifier %x" % self.identifier)
        with open(self.path, 'r+b') as img:
            img.seek(0x1B8)
            img.write(self.identifier.to_bytes(4, 'little'))

        msger.debug("Creating partitions")

        for part in self.partitions:
            if part.num == 0:
                continue

            if self.ptable_format == "msdos" and part.num == 5:
                # Create an extended partition (note: extended
                # partition is described in MBR and contains all
                # logical partitions). The logical partitions save a
                # sector for an EBR just before the start of a
                # partition. The extended partition must start one
                # sector before the start of the first logical
                # partition. This way the first EBR is inside of the
                # extended partition. Since the extended partitions
                # starts a sector before the first logical partition,
                # add a sector at the back, so that there is enough
                # room for all logical partitions.
                self._create_partition(self.path, "extended",
                                       None, part.start - 1,
                                       self.offset - part.start + 1)

            if part.fstype == "swap":
                parted_fs_type = "linux-swap"
            elif part.fstype == "vfat":
                parted_fs_type = "fat32"
            elif part.fstype == "msdos":
                parted_fs_type = "fat16"
            elif part.fstype == "ontrackdm6aux3":
                parted_fs_type = "ontrackdm6aux3"
            else:
                # Type for ext2/ext3/ext4/btrfs
                parted_fs_type = "ext2"

            # Boot ROM of OMAP boards require vfat boot partition to have an
            # even number of sectors.
            if part.mountpoint == "/boot" and part.fstype in ["vfat", "msdos"] \
               and part.size_sec % 2:
                msger.debug("Subtracting one sector from '%s' partition to " \
                            "get even number of sectors for the partition" % \
                            part.mountpoint)
                part.size_sec -= 1

            self._create_partition(self.path, part.type,
                                   parted_fs_type, part.start, part.size_sec)

            if part.part_type:
                msger.debug("partition %d: set type UID to %s" % \
                            (part.num, part.part_type))
                exec_native_cmd("sgdisk --typecode=%d:%s %s" % \
                                         (part.num, part.part_type,
                                          self.path), self.native_sysroot)

            if part.uuid and self.ptable_format == "gpt":
                msger.debug("partition %d: set UUID to %s" % \
                            (part.num, part.uuid))
                exec_native_cmd("sgdisk --partition-guid=%d:%s %s" % \
                                (part.num, part.uuid, self.path),
                                self.native_sysroot)

            if part.label and self.ptable_format == "gpt":
                msger.debug("partition %d: set name to %s" % \
                            (part.num, part.label))
                exec_native_cmd("parted -s %s name %d %s" % \
                                (self.path, part.num, part.label),
                                self.native_sysroot)

            if part.active:
                flag_name = "legacy_boot" if self.ptable_format == 'gpt' else "boot"
                msger.debug("Set '%s' flag for partition '%s' on disk '%s'" % \
                            (flag_name, part.num, self.path))
                exec_native_cmd("parted -s %s set %d %s on" % \
                                (self.path, part.num, flag_name),
                                self.native_sysroot)
            if part.system_id:
                exec_native_cmd("sfdisk --part-type %s %s %s" % \
                                (self.path, part.num, part.system_id),
                                self.native_sysroot)

            # Parted defaults to enabling the lba flag for fat16 partitions,
            # which causes compatibility issues with some firmware (and really
            # isn't necessary).
            if parted_fs_type == "fat16":
                if self.ptable_format == 'msdos':
                    msger.debug("Disable 'lba' flag for partition '%s' on disk '%s'" % \
                                (part.num, self.path))
                    exec_native_cmd("parted -s %s set %d lba off" % \
                                    (self.path, part.num),
                                    self.native_sysroot)

    def cleanup(self):
        # remove partition images
        for image in self.partimages:
            os.remove(image)

    def assemble(self):
        msger.debug("Installing partitions")

        for part in self.partitions:
            source = part.source_file
            if source:
                # install source_file contents into a partition
                sparse_copy(source, self.path, part.start * self.sector_size)

                msger.debug("Installed %s in partition %d, sectors %d-%d, "
                            "size %d sectors" % \
                            (source, part.num, part.start,
                             part.start + part.size_sec - 1, part.size_sec))

                partimage = self.path + '.p%d' % part.num
                os.rename(source, partimage)
                self.partimages.append(partimage)
