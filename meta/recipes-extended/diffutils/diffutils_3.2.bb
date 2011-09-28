DESCRIPTION = "Diffutils contains the GNU diff, diff3, \
sdiff, and cmp utilities. These programs are usually \
used for creating patch files."
SECTION = "base"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PR = "r0"

SRC_URI = "${GNU_MIRROR}/diffutils/diffutils-${PV}.tar.gz"

inherit autotools update-alternatives gettext

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_libc-uclibc = "--without-included-regex"

ALTERNATIVE_LINKS = "${bindir}/diff ${bindir}/cmp"
ALTERNATIVE_PRIORITY = "100"

SRC_URI[md5sum] = "22e4deef5d8949a727b159d6bc65c1cc"
SRC_URI[sha256sum] = "2aaaebef615be7dc365306a14caa5d273a4fc174f9f10abca8b60e082c054ed3"
