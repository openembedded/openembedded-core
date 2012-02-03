DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=4535377ede62550fdeaf39f595fd550a"
PR = "r1"

PACKAGES =+ "${PN}-graph "
FILES_${PN}-graph = "${bindir}/strace-graph"
RDEPENDS_${PN}-graph = "perl"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.xz \
           file://sigmask.patch \
	   file://strace-4.6_add_x32_support.patch \
          "

SRC_URI[md5sum] = "e537b2b1afeec70c0e6e27a0d0fd671e"
SRC_URI[sha256sum] = "9ef9aa41b6118578e33ef4833b8a04209d6cc062546c28efd715f283b172c28a"
inherit autotools

export INCLUDES = "-I. -I./linux"

BBCLASSEXTEND = "native"
