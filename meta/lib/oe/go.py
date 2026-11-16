#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import re

def map_arch(a):
    """
    Take ``a``, the architecture name used in OpenEmbedded (see the
    :term:`TUNE_ARCH` variable), and return a string for the mapped architecture
    name from Go's ``GOARCH`` names (see
    https://github.com/golang/go/blob/master/src/internal/syslist/syslist.go
    for the complete list).
    """
    if re.match('i.86', a):
        return '386'
    elif a == 'x86_64':
        return 'amd64'
    elif re.match('arm.*', a):
        return 'arm'
    elif re.match('aarch64.*', a):
        return 'arm64'
    elif re.match('mips64el.*', a):
        return 'mips64le'
    elif re.match('mips64.*', a):
        return 'mips64'
    elif a == 'mips':
        return 'mips'
    elif a == 'mipsel':
        return 'mipsle'
    elif re.match('p(pc|owerpc)(64le)', a):
        return 'ppc64le'
    elif re.match('p(pc|owerpc)(64)', a):
        return 'ppc64'
    elif a == 'riscv64':
        return 'riscv64'
    elif a == 'loongarch64':
        return 'loong64'
    raise bb.parse.SkipRecipe(f"Cannot map architecture {a}")
