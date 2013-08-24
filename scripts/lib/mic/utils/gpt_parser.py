#!/usr/bin/python -tt
#
# Copyright (c) 2013 Intel, Inc.
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

""" This module implements a simple GPT partitions parser which can read the
GPT header and the GPT partition table. """

import struct
import uuid
import binascii
from mic.utils.errors import MountError

_GPT_HEADER_FORMAT = "<8s4sIIIQQQQ16sQIII"
_GPT_HEADER_SIZE = struct.calcsize(_GPT_HEADER_FORMAT)
_GPT_ENTRY_FORMAT = "<16s16sQQQ72s"
_GPT_ENTRY_SIZE = struct.calcsize(_GPT_ENTRY_FORMAT)
_SUPPORTED_GPT_REVISION = '\x00\x00\x01\x00'

def _stringify_uuid(binary_uuid):
    """ A small helper function to transform a binary UUID into a string
    format. """

    uuid_str = str(uuid.UUID(bytes_le = binary_uuid))

    return uuid_str.upper()

def _calc_header_crc(raw_hdr):
    """ Calculate GPT header CRC32 checksum. The 'raw_hdr' parameter has to
    be a list or a tuple containing all the elements of the GPT header in a
    "raw" form, meaning that it should simply contain "unpacked" disk data.
    """

    raw_hdr = list(raw_hdr)
    raw_hdr[3] = 0
    raw_hdr = struct.pack(_GPT_HEADER_FORMAT, *raw_hdr)

    return binascii.crc32(raw_hdr) & 0xFFFFFFFF

def _validate_header(raw_hdr):
    """ Validate the GPT header. The 'raw_hdr' parameter has to be a list or a
    tuple containing all the elements of the GPT header in a "raw" form,
    meaning that it should simply contain "unpacked" disk data. """

    # Validate the signature
    if raw_hdr[0] != 'EFI PART':
        raise MountError("GPT partition table not found")

    # Validate the revision
    if raw_hdr[1] != _SUPPORTED_GPT_REVISION:
        raise MountError("Unsupported GPT revision '%s', supported revision " \
                         "is '%s'" % \
                          (binascii.hexlify(raw_hdr[1]),
                           binascii.hexlify(_SUPPORTED_GPT_REVISION)))

    # Validate header size
    if raw_hdr[2] != _GPT_HEADER_SIZE:
        raise MountError("Bad GPT header size: %d bytes, expected %d" % \
                         (raw_hdr[2], _GPT_HEADER_SIZE))

    crc = _calc_header_crc(raw_hdr)
    if raw_hdr[3] != crc:
        raise MountError("GPT header crc mismatch: %#x, should be %#x" % \
                         (crc, raw_hdr[3]))

class GptParser:
    """ GPT partition table parser. Allows reading the GPT header and the
    partition table, as well as modifying the partition table records. """

    def __init__(self, disk_path, sector_size = 512):
        """ The class constructor which accepts the following parameters:
            * disk_path - full path to the disk image or device node
            * sector_size - size of a disk sector in bytes """

        self.sector_size = sector_size
        self.disk_path = disk_path

        try:
            self._disk_obj = open(disk_path, 'r+b')
        except IOError as err:
            raise MountError("Cannot open file '%s' for reading GPT " \
                             "partitions: %s" % (disk_path, err))

    def __del__(self):
        """ The class destructor. """

        self._disk_obj.close()

    def _read_disk(self, offset, size):
        """ A helper function which reads 'size' bytes from offset 'offset' of
        the disk and checks all the error conditions. """

        self._disk_obj.seek(offset)
        try:
            data = self._disk_obj.read(size)
        except IOError as err:
            raise MountError("cannot read from '%s': %s" % \
                             (self.disk_path, err))

        if len(data) != size:
            raise MountError("cannot read %d bytes from offset '%d' of '%s', " \
                             "read only %d bytes" % \
                             (size, offset, self.disk_path, len(data)))

        return data

    def _write_disk(self, offset, buf):
        """ A helper function which writes buffer 'buf' to offset 'offset' of
        the disk. This function takes care of unaligned writes and checks all
        the error conditions. """

        # Since we may be dealing with a block device, we only can write in
        # 'self.sector_size' chunks. Find the aligned starting and ending
        # disk offsets to read.
        start =  (offset / self.sector_size) * self.sector_size
        end = ((start + len(buf)) / self.sector_size + 1) * self.sector_size

        data = self._read_disk(start, end - start)
        off = offset - start
        data = data[:off] + buf + data[off + len(buf):]

        self._disk_obj.seek(start)
        try:
            self._disk_obj.write(data)
        except IOError as err:
            raise MountError("cannot write to '%s': %s" % (self.disk_path, err))

    def read_header(self, primary = True):
        """ Read and verify the GPT header and return a dictionary containing
        the following elements:

        'signature'   : header signature
        'revision'    : header revision
        'hdr_size'    : header size in bytes
        'hdr_crc'     : header CRC32
        'hdr_lba'     : LBA of this header
        'hdr_offs'    : byte disk offset of this header
        'backup_lba'  : backup header LBA
        'backup_offs' : byte disk offset of backup header
        'first_lba'   : first usable LBA for partitions
        'first_offs'  : first usable byte disk offset for partitions
        'last_lba'    : last usable LBA for partitions
        'last_offs'   : last usable byte disk offset for partitions
        'disk_uuid'   : UUID of the disk
        'ptable_lba'  : starting LBA of array of partition entries
        'ptable_offs' : disk byte offset of the start of the partition table
        'ptable_size' : partition table size in bytes
        'entries_cnt' : number of available partition table entries
        'entry_size'  : size of a single partition entry
        'ptable_crc'  : CRC32 of the partition table
        'primary'     : a boolean, if 'True', this is the primary GPT header,
                        if 'False' - the secondary
        'primary_str' : contains string "primary" if this is the primary GPT
                        header, and "backup" otherwise

        This dictionary corresponds to the GPT header format. Please, see the
        UEFI standard for the description of these fields.

        If the 'primary' parameter is 'True', the primary GPT header is read,
        otherwise the backup GPT header is read instead. """

        # Read and validate the primary GPT header
        raw_hdr = self._read_disk(self.sector_size, _GPT_HEADER_SIZE)
        raw_hdr = struct.unpack(_GPT_HEADER_FORMAT, raw_hdr)
        _validate_header(raw_hdr)
        primary_str = "primary"

        if not primary:
            # Read and validate the backup GPT header
            raw_hdr = self._read_disk(raw_hdr[6] * self.sector_size, _GPT_HEADER_SIZE)
            raw_hdr = struct.unpack(_GPT_HEADER_FORMAT, raw_hdr)
            _validate_header(raw_hdr)
            primary_str = "backup"

        return { 'signature'   : raw_hdr[0],
                 'revision'    : raw_hdr[1],
                 'hdr_size'    : raw_hdr[2],
                 'hdr_crc'     : raw_hdr[3],
                 'hdr_lba'     : raw_hdr[5],
                 'hdr_offs'    : raw_hdr[5] * self.sector_size,
                 'backup_lba'  : raw_hdr[6],
                 'backup_offs' : raw_hdr[6] * self.sector_size,
                 'first_lba'   : raw_hdr[7],
                 'first_offs'  : raw_hdr[7] * self.sector_size,
                 'last_lba'    : raw_hdr[8],
                 'last_offs'   : raw_hdr[8] * self.sector_size,
                 'disk_uuid'   :_stringify_uuid(raw_hdr[9]),
                 'ptable_lba'  : raw_hdr[10],
                 'ptable_offs' : raw_hdr[10] * self.sector_size,
                 'ptable_size' : raw_hdr[11] * raw_hdr[12],
                 'entries_cnt' : raw_hdr[11],
                 'entry_size'  : raw_hdr[12],
                 'ptable_crc'  : raw_hdr[13],
                 'primary'     : primary,
                 'primary_str' : primary_str }

    def _read_raw_ptable(self, header):
        """ Read and validate primary or backup partition table. The 'header'
        argument is the GPT header. If it is the primary GPT header, then the
        primary partition table is read and validated, otherwise - the backup
        one. The 'header' argument is a dictionary which is returned by the
        'read_header()' method. """

        raw_ptable = self._read_disk(header['ptable_offs'],
                                     header['ptable_size'])

        crc = binascii.crc32(raw_ptable) & 0xFFFFFFFF
        if crc != header['ptable_crc']:
            raise MountError("Partition table at LBA %d (%s) is corrupted" % \
                             (header['ptable_lba'], header['primary_str']))

        return raw_ptable

    def get_partitions(self, primary = True):
        """ This is a generator which parses the GPT partition table and
        generates the following dictionary for each partition:

        'index'       : the index of the partition table endry
        'offs'        : byte disk offset of the partition table entry
        'type_uuid'   : partition type UUID
        'part_uuid'   : partition UUID
        'first_lba'   : the first LBA
        'last_lba'    : the last LBA
        'flags'       : attribute flags
        'name'        : partition name
        'primary'     : a boolean, if 'True', this is the primary partition
                        table, if 'False' - the secondary
        'primary_str' : contains string "primary" if this is the primary GPT
                        header, and "backup" otherwise

        This dictionary corresponds to the GPT header format. Please, see the
        UEFI standard for the description of these fields.

        If the 'primary' parameter is 'True', partitions from the primary GPT
        partition table are generated, otherwise partitions from the backup GPT
        partition table are generated. """

        if primary:
            primary_str = "primary"
        else:
            primary_str = "backup"

        header = self.read_header(primary)
        raw_ptable = self._read_raw_ptable(header)

        for index in xrange(0, header['entries_cnt']):
            start = header['entry_size'] * index
            end = start + header['entry_size']
            raw_entry = struct.unpack(_GPT_ENTRY_FORMAT, raw_ptable[start:end])

            if raw_entry[2] == 0 or raw_entry[3] == 0:
                continue

            part_name = str(raw_entry[5].decode('UTF-16').split('\0', 1)[0])

            yield { 'index'       : index,
                    'offs'        : header['ptable_offs'] + start,
                    'type_uuid'   : _stringify_uuid(raw_entry[0]),
                    'part_uuid'   : _stringify_uuid(raw_entry[1]),
                    'first_lba'   : raw_entry[2],
                    'last_lba'    : raw_entry[3],
                    'flags'       : raw_entry[4],
                    'name'        : part_name,
                    'primary'     : primary,
                    'primary_str' : primary_str }

    def _change_partition(self, header, entry):
        """ A helper function for 'change_partitions()' which changes a
        a paricular instance of the partition table (primary or backup). """

        if entry['index'] >= header['entries_cnt']:
            raise MountError("Partition table at LBA %d has only %d "   \
                             "records cannot change record number %d" % \
                             (header['entries_cnt'], entry['index']))
        # Read raw GPT header
        raw_hdr = self._read_disk(header['hdr_offs'], _GPT_HEADER_SIZE)
        raw_hdr = list(struct.unpack(_GPT_HEADER_FORMAT, raw_hdr))
        _validate_header(raw_hdr)

        # Prepare the new partition table entry
        raw_entry = struct.pack(_GPT_ENTRY_FORMAT,
                                uuid.UUID(entry['type_uuid']).bytes_le,
                                uuid.UUID(entry['part_uuid']).bytes_le,
                                entry['first_lba'],
                                entry['last_lba'],
                                entry['flags'],
                                entry['name'].encode('UTF-16'))

        # Write the updated entry to the disk
        entry_offs = header['ptable_offs'] + \
                     header['entry_size'] * entry['index']
        self._write_disk(entry_offs, raw_entry)

        # Calculate and update partition table CRC32
        raw_ptable = self._read_disk(header['ptable_offs'],
                                     header['ptable_size'])
        raw_hdr[13] = binascii.crc32(raw_ptable) & 0xFFFFFFFF

        # Calculate and update the GPT header CRC
        raw_hdr[3] = _calc_header_crc(raw_hdr)

        # Write the updated header to the disk
        raw_hdr = struct.pack(_GPT_HEADER_FORMAT, *raw_hdr)
        self._write_disk(header['hdr_offs'], raw_hdr)

    def change_partition(self, entry):
        """ Change a GPT partition. The 'entry' argument has the same format as
        'get_partitions()' returns. This function simply changes the partition
        table record corresponding to 'entry' in both, the primary and the
        backup GPT partition tables. The parition table CRC is re-calculated
        and the GPT headers are modified accordingly. """

        # Change the primary partition table
        header = self.read_header(True)
        self._change_partition(header, entry)

        # Change the backup partition table
        header = self.read_header(False)
        self._change_partition(header, entry)
