#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import re

# parse kernel ABI version out of <linux/version.h>
def get_version_headers(p):
    fn = p + '/include/linux/utsrelease.h'
    if not os.path.isfile(fn):
        # after 2.6.33-rc1
        fn = p + '/include/generated/utsrelease.h'
    if not os.path.isfile(fn):
        fn = p + '/include/linux/version.h'

    try:
        f = open(fn, 'r')
    except IOError:
        return None

    l = f.readlines()
    f.close()
    r = re.compile("#define UTS_RELEASE \"(.*)\"")
    for s in l:
        m = r.match(s)
        if m:
            return m.group(1)
    return None


def get_version_file(p):
    fn = p + '/kernel-abiversion'

    try:
        with open(fn, 'r') as f:
            return f.readlines()[0].strip()
    except IOError:
        return None

def get_localversion_file(p):
    fn = p + '/kernel-localversion'

    try:
        with open(fn, 'r') as f:
            return f.readlines()[0].strip()
    except IOError:
        return ""

    return ""
