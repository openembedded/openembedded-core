#!/usr/bin/python -tt
#
# Copyright (c) 2010, 2011 Intel Inc.
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
import sys
import time
import tempfile
import re
import shutil
import glob
import hashlib
import subprocess
import platform
import traceback


try:
    import sqlite3 as sqlite
except ImportError:
    import sqlite

try:
    from xml.etree import cElementTree
except ImportError:
    import cElementTree
xmlparse = cElementTree.parse

from mic import msger
from mic.utils.errors import CreatorError, SquashfsError
from mic.utils.fs_related import find_binary_path, makedirs
from mic.utils import runner

def build_name(kscfg, release=None, prefix = None, suffix = None):
    """Construct and return an image name string.

    This is a utility function to help create sensible name and fslabel
    strings. The name is constructed using the sans-prefix-and-extension
    kickstart filename and the supplied prefix and suffix.

    kscfg -- a path to a kickstart file
    release --  a replacement to suffix for image release
    prefix -- a prefix to prepend to the name; defaults to None, which causes
              no prefix to be used
    suffix -- a suffix to append to the name; defaults to None, which causes
              a YYYYMMDDHHMM suffix to be used

    Note, if maxlen is less then the len(suffix), you get to keep both pieces.

    """
    name = os.path.basename(kscfg)
    idx = name.rfind('.')
    if idx >= 0:
        name = name[:idx]

    if release is not None:
        suffix = ""
    if prefix is None:
        prefix = ""
    if suffix is None:
        suffix = time.strftime("%Y%m%d%H%M")

    if name.startswith(prefix):
        name = name[len(prefix):]

    prefix = "%s-" % prefix if prefix else ""
    suffix = "-%s" % suffix if suffix else ""

    ret = prefix + name + suffix
    return ret

def get_distro():
    """Detect linux distribution, support "meego"
    """

    support_dists = ('SuSE',
                     'debian',
                     'fedora',
                     'redhat',
                     'centos',
                     'meego',
                     'moblin',
                     'tizen')
    try:
        (dist, ver, id) = platform.linux_distribution( \
                              supported_dists = support_dists)
    except:
        (dist, ver, id) = platform.dist( \
                              supported_dists = support_dists)

    return (dist, ver, id)

def get_distro_str():
    """Get composited string for current linux distribution
    """
    (dist, ver, id) = get_distro()

    if not dist:
        return 'Unknown Linux Distro'
    else:
        distro_str = ' '.join(map(str.strip, (dist, ver, id)))
        return distro_str.strip()

_LOOP_RULE_PTH = None

def human_size(size):
    """Return human readable string for Bytes size
    """

    if size <= 0:
        return "0M"
    import math
    measure = ['B', 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y']
    expo = int(math.log(size, 1024))
    mant = float(size/math.pow(1024, expo))
    return "{0:.1f}{1:s}".format(mant, measure[expo])

def get_block_size(file_obj):
    """ Returns block size for file object 'file_obj'. Errors are indicated by
    the 'IOError' exception. """

    from fcntl import ioctl
    import struct

    # Get the block size of the host file-system for the image file by calling
    # the FIGETBSZ ioctl (number 2).
    binary_data = ioctl(file_obj, 2, struct.pack('I', 0))
    return struct.unpack('I', binary_data)[0]

def check_space_pre_cp(src, dst):
    """Check whether disk space is enough before 'cp' like
    operations, else exception will be raised.
    """

    srcsize  = get_file_size(src) * 1024 * 1024
    freesize = get_filesystem_avail(dst)
    if srcsize > freesize:
        raise CreatorError("space on %s(%s) is not enough for about %s files"
                           % (dst, human_size(freesize), human_size(srcsize)))

def calc_hashes(file_path, hash_names, start = 0, end = None):
    """ Calculate hashes for a file. The 'file_path' argument is the file
    to calculate hash functions for, 'start' and 'end' are the starting and
    ending file offset to calculate the has functions for. The 'hash_names'
    argument is a list of hash names to calculate. Returns the the list
    of calculated hash values in the hexadecimal form in the same order
    as 'hash_names'.
    """
    if end == None:
        end = os.path.getsize(file_path)

    chunk_size = 65536
    to_read = end - start
    read = 0

    hashes = []
    for hash_name in hash_names:
        hashes.append(hashlib.new(hash_name))

    with open(file_path, "rb") as f:
        f.seek(start)

        while read < to_read:
            if read + chunk_size > to_read:
                chunk_size = to_read - read
            chunk = f.read(chunk_size)
            for hash_obj in hashes:
                hash_obj.update(chunk)
            read += chunk_size

    result = []
    for hash_obj in hashes:
        result.append(hash_obj.hexdigest())

    return result

def get_md5sum(fpath):
    return calc_hashes(fpath, ('md5', ))[0]


def normalize_ksfile(ksconf, release, arch):
    '''
    Return the name of a normalized ks file in which macro variables
    @BUILD_ID@ and @ARCH@ are replace with real values.

    The original ks file is returned if no special macro is used, otherwise
    a temp file is created and returned, which will be deleted when program
    exits normally.
    '''

    if not release:
        release = "latest"
    if not arch or re.match(r'i.86', arch):
        arch = "ia32"

    with open(ksconf) as f:
        ksc = f.read()

    if "@ARCH@" not in ksc and "@BUILD_ID@" not in ksc:
        return ksconf

    msger.info("Substitute macro variable @BUILD_ID@/@ARCH@ in ks: %s" % ksconf)
    ksc = ksc.replace("@ARCH@", arch)
    ksc = ksc.replace("@BUILD_ID@", release)

    fd, ksconf = tempfile.mkstemp(prefix=os.path.basename(ksconf))
    os.write(fd, ksc)
    os.close(fd)

    msger.debug('normalized ks file:%s' % ksconf)

    def remove_temp_ks():
        try:
            os.unlink(ksconf)
        except OSError, err:
            msger.warning('Failed to remove temp ks file:%s:%s' % (ksconf, err))

    import atexit
    atexit.register(remove_temp_ks)

    return ksconf


def selinux_check(arch, fstypes):
    try:
        getenforce = find_binary_path('getenforce')
    except CreatorError:
        return

    selinux_status = runner.outs([getenforce])
    if arch and arch.startswith("arm") and selinux_status == "Enforcing":
        raise CreatorError("Can't create arm image if selinux is enabled, "
                           "please run 'setenforce 0' to disable selinux")

    use_btrfs = filter(lambda typ: typ == 'btrfs', fstypes)
    if use_btrfs and selinux_status == "Enforcing":
        raise CreatorError("Can't create btrfs image if selinux is enabled,"
                           " please run 'setenforce 0' to disable selinux")

def get_file_size(filename):
    """ Return size in MB unit """
    cmd = ['du', "-s", "-b", "-B", "1M", filename]
    rc, duOutput  = runner.runtool(cmd)
    if rc != 0:
        raise CreatorError("Failed to run: %s" % ' '.join(cmd))
    size1 = int(duOutput.split()[0])

    cmd = ['du', "-s", "-B", "1M", filename]
    rc, duOutput = runner.runtool(cmd)
    if rc != 0:
        raise CreatorError("Failed to run: %s" % ' '.join(cmd))

    size2 = int(duOutput.split()[0])
    return max(size1, size2)


def get_filesystem_avail(fs):
    vfstat = os.statvfs(fs)
    return vfstat.f_bavail * vfstat.f_bsize

def mkdtemp(dir = "/var/tmp", prefix = "wic-tmp-"):
    """ FIXME: use the dir in wic.conf instead """

    makedirs(dir)
    return tempfile.mkdtemp(dir = dir, prefix = prefix)

def strip_end(text, suffix):
    if not text.endswith(suffix):
        return text
    return text[:-len(suffix)]
