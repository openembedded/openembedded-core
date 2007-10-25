DESCRIPTION = "strace is a system call tracing tool."
SECTION = "console/utils"
LICENSE = "GPL"
PR = "r4"

SRC_URI = "${SOURCEFORGE_MIRROR}/strace/strace-${PV}.tar.bz2 \
           file://glibc-2.5.patch;patch=1 \
           file://arm-eabi.patch;patch=1 \
           file://drop-ctl-proc.patch;patch=1 \
           file://sh-arch-update.patch;patch=1 \
           file://sh-syscall-update.patch;patch=1 \
           file://strace-fix-arm-bad-syscall.patch;patch=1 \
           file://strace-undef-syscall.patch;patch=1 "

inherit autotools

export INCLUDES = "-I. -I./linux"
