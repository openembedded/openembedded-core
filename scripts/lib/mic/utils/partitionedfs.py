#!/usr/bin/python -tt
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

from mic import msger
from mic.utils import runner
from mic.utils.errors import MountError
from mic.utils.fs_related import *
from mic.utils.gpt_parser import GptParser
from mic.utils.oe.misc import *

# Overhead of the MBR partitioning scheme (just one sector)
MBR_OVERHEAD = 1
# Overhead of the GPT partitioning scheme
GPT_OVERHEAD = 34

# Size of a sector in bytes
SECTOR_SIZE = 512

class PartitionedMount(Mount):
    def __init__(self, mountdir, skipformat = False):
        Mount.__init__(self, mountdir)
        self.disks = {}
        self.partitions = []
        self.subvolumes = []
        self.mapped = False
        self.mountOrder = []
        self.unmountOrder = []
        self.parted = find_binary_path("parted")
        self.btrfscmd=None
        self.skipformat = skipformat
        self.snapshot_created = self.skipformat
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
                  'mapped': False,  # True if kpartx mapping exists
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

        # We need to handle subvolumes for btrfs
        if fstype == "btrfs" and fsopts and fsopts.find("subvol=") != -1:
            self.btrfscmd=find_binary_path("btrfs")
            subvol = None
            opts = fsopts.split(",")
            for opt in opts:
                if opt.find("subvol=") != -1:
                    subvol = opt.replace("subvol=", "").strip()
                    break
            if not subvol:
                raise MountError("No subvolume: %s" % fsopts)
            self.subvolumes.append({'size': size, # In sectors
                                    'mountpoint': mountpoint, # Mount relative to chroot
                                    'fstype': fstype, # Filesystem type
                                    'fsopts': fsopts, # Filesystem mount options
                                    'disk_name': disk_name, # physical disk name holding partition
                                    'device': None, # kpartx device node for partition
                                    'mount': None, # Mount object
                                    'subvol': subvol, # Subvolume name
                                    'boot': boot, # Bootable flag
                                    'mounted': False # Mount flag
                                   })

        # We still need partition for "/" or non-subvolume
        if mountpoint == "/" or not fsopts or fsopts.find("subvol=") == -1:
            # Don't need subvolume for "/" because it will be set as default subvolume
            if fsopts and fsopts.find("subvol=") != -1:
                opts = fsopts.split(",")
                for opt in opts:
                    if opt.strip().startswith("subvol="):
                        opts.remove(opt)
                        break
                fsopts = ",".join(opts)

            part = { 'ks_pnum' : ks_pnum, # Partition number in the KS file
                     'size': size, # In sectors
                     'mountpoint': mountpoint, # Mount relative to chroot
                     'source_file': source_file, # partition contents
                     'fstype': fstype, # Filesystem type
                     'fsopts': fsopts, # Filesystem mount options
                     'label': label, # Partition label
                     'disk_name': disk_name, # physical disk name holding partition
                     'device': None, # kpartx device node for partition
                     'mount': None, # Mount object
                     'num': None, # Partition number
                     'boot': boot, # Bootable flag
                     'align': align, # Partition alignment
                     'part_type' : part_type, # Partition type
                     'partuuid': None } # Partition UUID (GPT-only)

            self.__add_partition(part)

    def layout_partitions(self, ptable_format = "msdos"):
        """ Layout the partitions, meaning calculate the position of every
        partition on the disk. The 'ptable_format' parameter defines the
        partition table format, and may be either "msdos" or "gpt". """

        msger.debug("Assigning %s partitions to disks" % ptable_format)

        if ptable_format not in ('msdos', 'gpt'):
            raise MountError("Unknown partition table format '%s', supported " \
                             "formats are: 'msdos' and 'gpt'" % ptable_format)

        if self._partitions_layed_out:
            return

        self._partitions_layed_out = True

        # Go through partitions in the order they are added in .ks file
        for n in range(len(self.partitions)):
            p = self.partitions[n]

            if not self.disks.has_key(p['disk_name']):
                raise MountError("No disk %s for partition %s" \
                                 % (p['disk_name'], p['mountpoint']))

            if p['part_type'] and ptable_format != 'gpt':
                # The --part-type can also be implemented for MBR partitions,
                # in which case it would map to the 1-byte "partition type"
                # filed at offset 3 of the partition entry.
                raise MountError("setting custom partition type is only " \
                                 "imlemented for GPT partitions")

            # Get the disk where the partition is located
            d = self.disks[p['disk_name']]
            d['numpart'] += 1
            d['ptable_format'] = ptable_format

            if d['numpart'] == 1:
                if ptable_format == "msdos":
                    overhead = MBR_OVERHEAD
                else:
                    overhead = GPT_OVERHEAD

                # Skip one sector required for the partitioning scheme overhead
                d['offset'] += overhead
                # Steal few sectors from the first partition to offset for the
                # partitioning overhead
                p['size'] -= overhead

            if p['align']:
                # If not first partition and we do have alignment set we need
                # to align the partition.
                # FIXME: This leaves a empty spaces to the disk. To fill the
                # gaps we could enlargea the previous partition?

                # Calc how much the alignment is off.
                align_sectors = d['offset'] % (p['align'] * 1024 / self.sector_size)
                # We need to move forward to the next alignment point
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
            if d['ptable_format'] == 'gpt':
                # Account for the backup partition table at the end of the disk
                d['min_size'] += GPT_OVERHEAD

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
            msger.debug("WARNING: parted returned '%s' instead of 0" % rc)

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

        if self.skipformat:
            msger.debug("Skipping disk format, because skipformat flag is set.")
            return

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
                if d['ptable_format'] == 'gpt':
                    flag_name = "legacy_boot"
                else:
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

        # If the partition table format is "gpt", find out PARTUUIDs for all
        # the partitions. And if users specified custom parition type UUIDs,
        # set them.
        for disk_name, disk in self.disks.items():
            if disk['ptable_format'] != 'gpt':
                continue

            pnum = 0
            gpt_parser = GptParser(d['disk'].device, SECTOR_SIZE)
            # Iterate over all GPT partitions on this disk
            for entry in gpt_parser.get_partitions():
                pnum += 1
                # Find the matching partition in the 'self.partitions' list
                for n in d['partitions']:
                    p = self.partitions[n]
                    if p['num'] == pnum:
                        # Found, fetch PARTUUID (partition's unique ID)
                        p['partuuid'] = entry['part_uuid']
                        msger.debug("PARTUUID for partition %d on disk '%s' " \
                                    "(mount point '%s') is '%s'" % (pnum, \
                                    disk_name, p['mountpoint'], p['partuuid']))
                        if p['part_type']:
                            entry['type_uuid'] = p['part_type']
                            msger.debug("Change type of partition %d on disk " \
                                        "'%s' (mount point '%s') to '%s'" % \
                                        (pnum, disk_name, p['mountpoint'],
                                         p['part_type']))
                            gpt_parser.change_partition(entry)

            del gpt_parser

    def __map_partitions(self):
        """Load it if dm_snapshot isn't loaded. """
        load_module("dm_snapshot")

        for dev in self.disks.keys():
            d = self.disks[dev]
            if d['mapped']:
                continue

            msger.debug("Running kpartx on %s" % d['disk'].device )
            rc, kpartxOutput = runner.runtool([self.kpartx, "-l", "-v", d['disk'].device])
            kpartxOutput = kpartxOutput.splitlines()

            if rc != 0:
                raise MountError("Failed to query partition mapping for '%s'" %
                                 d['disk'].device)

            # Strip trailing blank and mask verbose output
            i = 0
            while i < len(kpartxOutput) and kpartxOutput[i][0:4] != "loop":
                i = i + 1
            kpartxOutput = kpartxOutput[i:]

            # Make sure kpartx reported the right count of partitions
            if len(kpartxOutput) != d['numpart']:
                # If this disk has more than 3 partitions, then in case of MBR
                # paritions there is an extended parition. Different versions
                # of kpartx behave differently WRT the extended partition -
                # some map it, some ignore it. This is why we do the below hack
                # - if kpartx reported one more partition and the partition
                # table type is "msdos" and the amount of partitions is more
                # than 3, we just assume kpartx mapped the extended parition
                # and we remove it.
                if len(kpartxOutput) == d['numpart'] + 1 \
                   and d['ptable_format'] == 'msdos' and len(kpartxOutput) > 3:
                    kpartxOutput.pop(3)
                else:
                    raise MountError("Unexpected number of partitions from " \
                                     "kpartx: %d != %d" % \
                                        (len(kpartxOutput), d['numpart']))

            for i in range(len(kpartxOutput)):
                line = kpartxOutput[i]
                newdev = line.split()[0]
                mapperdev = "/dev/mapper/" + newdev
                loopdev = d['disk'].device + newdev[-1]

                msger.debug("Dev %s: %s -> %s" % (newdev, loopdev, mapperdev))
                pnum = d['partitions'][i]
                self.partitions[pnum]['device'] = loopdev

                # grub's install wants partitions to be named
                # to match their parent device + partition num
                # kpartx doesn't work like this, so we add compat
                # symlinks to point to /dev/mapper
                if os.path.lexists(loopdev):
                    os.unlink(loopdev)
                os.symlink(mapperdev, loopdev)

            msger.debug("Adding partx mapping for %s" % d['disk'].device)
            rc = runner.show([self.kpartx, "-v", "-a", d['disk'].device])

            if rc != 0:
                # Make sure that the device maps are also removed on error case.
                # The d['mapped'] isn't set to True if the kpartx fails so
                # failed mapping will not be cleaned on cleanup either.
                runner.quiet([self.kpartx, "-d", d['disk'].device])
                raise MountError("Failed to map partitions for '%s'" %
                                 d['disk'].device)

            # FIXME: there is a bit delay for multipath device setup,
            # wait 10ms for the setup
            import time
            time.sleep(10)
            d['mapped'] = True

    def __unmap_partitions(self):
        for dev in self.disks.keys():
            d = self.disks[dev]
            if not d['mapped']:
                continue

            msger.debug("Removing compat symlinks")
            for pnum in d['partitions']:
                if self.partitions[pnum]['device'] != None:
                    os.unlink(self.partitions[pnum]['device'])
                    self.partitions[pnum]['device'] = None

            msger.debug("Unmapping %s" % d['disk'].device)
            rc = runner.quiet([self.kpartx, "-d", d['disk'].device])
            if rc != 0:
                raise MountError("Failed to unmap partitions for '%s'" %
                                 d['disk'].device)

            d['mapped'] = False

    def __calculate_mountorder(self):
        msger.debug("Calculating mount order")
        for p in self.partitions:
            if p['mountpoint']:
                self.mountOrder.append(p['mountpoint'])
                self.unmountOrder.append(p['mountpoint'])

        self.mountOrder.sort()
        self.unmountOrder.sort()
        self.unmountOrder.reverse()

    def cleanup(self):
        Mount.cleanup(self)
        if self.disks:
            self.__unmap_partitions()
            for dev in self.disks.keys():
                d = self.disks[dev]
                try:
                    d['disk'].cleanup()
                except:
                    pass

    def unmount(self):
        self.__unmount_subvolumes()
        for mp in self.unmountOrder:
            if mp == 'swap':
                continue
            p = None
            for p1 in self.partitions:
                if p1['mountpoint'] == mp:
                    p = p1
                    break

            if p['mount'] != None:
                try:
                    # Create subvolume snapshot here
                    if p['fstype'] == "btrfs" and p['mountpoint'] == "/" and not self.snapshot_created:
                        self.__create_subvolume_snapshots(p, p["mount"])
                    p['mount'].cleanup()
                except:
                    pass
                p['mount'] = None

    # Only for btrfs
    def __get_subvolume_id(self, rootpath, subvol):
        if not self.btrfscmd:
            self.btrfscmd=find_binary_path("btrfs")
        argv = [ self.btrfscmd, "subvolume", "list", rootpath ]

        rc, out = runner.runtool(argv)
        msger.debug(out)

        if rc != 0:
            raise MountError("Failed to get subvolume id from %s', return code: %d." % (rootpath, rc))

        subvolid = -1
        for line in out.splitlines():
            if line.endswith(" path %s" % subvol):
                subvolid = line.split()[1]
                if not subvolid.isdigit():
                    raise MountError("Invalid subvolume id: %s" % subvolid)
                subvolid = int(subvolid)
                break
        return subvolid

    def __create_subvolume_metadata(self, p, pdisk):
        if len(self.subvolumes) == 0:
            return

        argv = [ self.btrfscmd, "subvolume", "list", pdisk.mountdir ]
        rc, out = runner.runtool(argv)
        msger.debug(out)

        if rc != 0:
            raise MountError("Failed to get subvolume id from %s', return code: %d." % (pdisk.mountdir, rc))

        subvolid_items = out.splitlines()
        subvolume_metadata = ""
        for subvol in self.subvolumes:
            for line in subvolid_items:
                if line.endswith(" path %s" % subvol["subvol"]):
                    subvolid = line.split()[1]
                    if not subvolid.isdigit():
                        raise MountError("Invalid subvolume id: %s" % subvolid)

                    subvolid = int(subvolid)
                    opts = subvol["fsopts"].split(",")
                    for opt in opts:
                        if opt.strip().startswith("subvol="):
                            opts.remove(opt)
                            break
                    fsopts = ",".join(opts)
                    subvolume_metadata += "%d\t%s\t%s\t%s\n" % (subvolid, subvol["subvol"], subvol['mountpoint'], fsopts)

        if subvolume_metadata:
            fd = open("%s/.subvolume_metadata" % pdisk.mountdir, "w")
            fd.write(subvolume_metadata)
            fd.close()

    def __get_subvolume_metadata(self, p, pdisk):
        subvolume_metadata_file = "%s/.subvolume_metadata" % pdisk.mountdir
        if not os.path.exists(subvolume_metadata_file):
            return

        fd = open(subvolume_metadata_file, "r")
        content = fd.read()
        fd.close()

        for line in content.splitlines():
            items = line.split("\t")
            if items and len(items) == 4:
                self.subvolumes.append({'size': 0, # In sectors
                                        'mountpoint': items[2], # Mount relative to chroot
                                        'fstype': "btrfs", # Filesystem type
                                        'fsopts': items[3] + ",subvol=%s" %  items[1], # Filesystem mount options
                                        'disk_name': p['disk_name'], # physical disk name holding partition
                                        'device': None, # kpartx device node for partition
                                        'mount': None, # Mount object
                                        'subvol': items[1], # Subvolume name
                                        'boot': False, # Bootable flag
                                        'mounted': False # Mount flag
                                   })

    def __create_subvolumes(self, p, pdisk):
        """ Create all the subvolumes. """

        for subvol in self.subvolumes:
            argv = [ self.btrfscmd, "subvolume", "create", pdisk.mountdir + "/" + subvol["subvol"]]

            rc = runner.show(argv)
            if rc != 0:
                raise MountError("Failed to create subvolume '%s', return code: %d." % (subvol["subvol"], rc))

        # Set default subvolume, subvolume for "/" is default
        subvol = None
        for subvolume in self.subvolumes:
            if subvolume["mountpoint"] == "/" and p['disk_name'] == subvolume['disk_name']:
                subvol = subvolume
                break

        if subvol:
            # Get default subvolume id
            subvolid = self. __get_subvolume_id(pdisk.mountdir, subvol["subvol"])
            # Set default subvolume
            if subvolid != -1:
                rc = runner.show([ self.btrfscmd, "subvolume", "set-default", "%d" % subvolid, pdisk.mountdir])
                if rc != 0:
                    raise MountError("Failed to set default subvolume id: %d', return code: %d." % (subvolid, rc))

        self.__create_subvolume_metadata(p, pdisk)

    def __mount_subvolumes(self, p, pdisk):
        if self.skipformat:
            # Get subvolume info
            self.__get_subvolume_metadata(p, pdisk)
            # Set default mount options
            if len(self.subvolumes) != 0:
                for subvol in self.subvolumes:
                    if subvol["mountpoint"] == p["mountpoint"] == "/":
                        opts = subvol["fsopts"].split(",")
                        for opt in opts:
                            if opt.strip().startswith("subvol="):
                                opts.remove(opt)
                                break
                        pdisk.fsopts = ",".join(opts)
                        break

        if len(self.subvolumes) == 0:
            # Return directly if no subvolumes
            return

        # Remount to make default subvolume mounted
        rc = runner.show([self.umountcmd, pdisk.mountdir])
        if rc != 0:
            raise MountError("Failed to umount %s" % pdisk.mountdir)

        rc = runner.show([self.mountcmd, "-o", pdisk.fsopts, pdisk.disk.device, pdisk.mountdir])
        if rc != 0:
            raise MountError("Failed to umount %s" % pdisk.mountdir)

        for subvol in self.subvolumes:
            if subvol["mountpoint"] == "/":
                continue
            subvolid = self. __get_subvolume_id(pdisk.mountdir, subvol["subvol"])
            if subvolid == -1:
                msger.debug("WARNING: invalid subvolume %s" % subvol["subvol"])
                continue
            # Replace subvolume name with subvolume ID
            opts = subvol["fsopts"].split(",")
            for opt in opts:
                if opt.strip().startswith("subvol="):
                    opts.remove(opt)
                    break

            opts.extend(["subvolrootid=0", "subvol=%s" % subvol["subvol"]])
            fsopts = ",".join(opts)
            subvol['fsopts'] = fsopts
            mountpoint = self.mountdir + subvol['mountpoint']
            makedirs(mountpoint)
            rc = runner.show([self.mountcmd, "-o", fsopts, pdisk.disk.device, mountpoint])
            if rc != 0:
                raise MountError("Failed to mount subvolume %s to %s" % (subvol["subvol"], mountpoint))
            subvol["mounted"] = True

    def __unmount_subvolumes(self):
        """ It may be called multiple times, so we need to chekc if it is still mounted. """
        for subvol in self.subvolumes:
            if subvol["mountpoint"] == "/":
                continue
            if not subvol["mounted"]:
                continue
            mountpoint = self.mountdir + subvol['mountpoint']
            rc = runner.show([self.umountcmd, mountpoint])
            if rc != 0:
                raise MountError("Failed to unmount subvolume %s from %s" % (subvol["subvol"], mountpoint))
            subvol["mounted"] = False

    def __create_subvolume_snapshots(self, p, pdisk):
        import time

        if self.snapshot_created:
            return

        # Remount with subvolid=0
        rc = runner.show([self.umountcmd, pdisk.mountdir])
        if rc != 0:
            raise MountError("Failed to umount %s" % pdisk.mountdir)
        if pdisk.fsopts:
            mountopts = pdisk.fsopts + ",subvolid=0"
        else:
            mountopts = "subvolid=0"
        rc = runner.show([self.mountcmd, "-o", mountopts, pdisk.disk.device, pdisk.mountdir])
        if rc != 0:
            raise MountError("Failed to umount %s" % pdisk.mountdir)

        # Create all the subvolume snapshots
        snapshotts = time.strftime("%Y%m%d-%H%M")
        for subvol in self.subvolumes:
            subvolpath = pdisk.mountdir + "/" + subvol["subvol"]
            snapshotpath = subvolpath + "_%s-1" % snapshotts
            rc = runner.show([ self.btrfscmd, "subvolume", "snapshot", subvolpath, snapshotpath ])
            if rc != 0:
                raise MountError("Failed to create subvolume snapshot '%s' for '%s', return code: %d." % (snapshotpath, subvolpath, rc))

        self.snapshot_created = True

    def __install_partition(self, num, source_file, start, size):
        """
        Install source_file contents into a partition.
        """
        if not source_file: # nothing to install
            return

        # Start is included in the size so need to substract one from the end.
        end = start + size - 1
        msger.debug("Installed %s in partition %d, sectors %d-%d, size %d sectors" % (source_file, num, start, end, size))

        dd_cmd = "dd if=%s of=%s bs=%d seek=%d count=%d conv=notrunc" % \
            (source_file, self.image_file, self.sector_size, start, size)
        rc, out = exec_cmd(dd_cmd)


    def install(self, image_file):
        msger.debug("Installing partitions")

        self.image_file = image_file

        for p in self.partitions:
            d = self.disks[p['disk_name']]
            if d['ptable_format'] == "msdos" and p['num'] == 5:
                # The last sector of the 3rd partition was reserved for the EBR
                # of the first _logical_ partition. This is why the extended
                # partition should start one sector before the first logical
                # partition.
                self.__install_partition(p['num'], p['source_file'],
                                         p['start'] - 1,
                                         d['offset'] - p['start'])

            self.__install_partition(p['num'], p['source_file'],
                                     p['start'], p['size'])

    def mount(self):
        for dev in self.disks.keys():
            d = self.disks[dev]
            d['disk'].create()

        self.__format_disks()

        self.__calculate_mountorder()

        return

    def resparse(self, size = None):
        # Can't re-sparse a disk image - too hard
        pass
