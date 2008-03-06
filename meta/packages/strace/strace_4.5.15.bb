DESCRIPTION = "strace is a system call tracing tool."
SECTION = "console/utils"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2 \
           file://strace-fix-arm-bad-syscall.patch;patch=1 \
           file://strace-undef-syscall.patch;patch=1 \
	   file://linux-headers-fix.patch;patch=1"
# TODO file://sh-arch-update.patch;patch=1 \
# TODO file://sh-syscall-update.patch;patch=1 \

inherit autotools

export INCLUDES = "-I. -I./linux"
