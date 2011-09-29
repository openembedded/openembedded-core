DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=4535377ede62550fdeaf39f595fd550a"
PR = "r2"

RDEPENDS = "perl"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2 \
           file://sigmask.patch \
          "

SRC_URI[md5sum] = "64dfe10d9db0c1e34030891695ffca4b"
SRC_URI[sha256sum] = "ea8c059369eaa5ad90b246f34eab247d0ee48bfdee2670c7196320a4669ccabd"
inherit autotools

export INCLUDES = "-I. -I./linux"

BBCLASSEXTEND = "native"
