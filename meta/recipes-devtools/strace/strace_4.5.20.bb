DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://strace.sourceforge.net"
SECTION = "console/utils"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=4535377ede62550fdeaf39f595fd550a"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2"
inherit autotools

export INCLUDES = "-I. -I./linux"

BBCLASSEXTEND = "native"
