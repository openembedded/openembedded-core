SUMMARY = "Diffutils contains tools used for finding differences between files."
DESCRIPTION = "Diffutils contains the GNU diff, diff3, \
sdiff, and cmp utilities. These programs are usually \
used for creating patch files."
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PR = "r4"

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.gz"

SRC_URI[md5sum] = "71f9c5ae19b60608f6c7f162da86a428"
SRC_URI[sha256sum] = "c5001748b069224dd98bf1bb9ee877321c7de8b332c8aad5af3e2a7372d23f5a"

inherit autotools update-alternatives gettext

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_libc-uclibc = "--without-included-regex"

ALTERNATIVE_LINKS = "${bindir}/diff ${bindir}/cmp"
ALTERNATIVE_PRIORITY = "100"
