DESCRIPTION = "strace is a system call tracing tool."
HOMEPAGE = "http://sourceforge.net/projects/strace/"
SECTION = "console/utils"
LICENSE = "BSD"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2"
inherit autotools

export INCLUDES = "-I. -I./linux"
