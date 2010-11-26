DESCRIPTION = "Diffutils contains the GNU diff, diff3, \
sdiff, and cmp utilities. These programs are usually \
used for creating patch files."
SECTION = "base"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

PR = "r4"

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.gz"

inherit autotools update-alternatives gettext

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_linux-uclibc = "--without-included-regex"

ALTERNATIVE_LINKS = "${bindir}/diff ${bindir}/cmp"
ALTERNATIVE_PRIORITY = "100"
