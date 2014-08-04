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

from mic import msger
from mic.utils.errors import CreatorError
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
