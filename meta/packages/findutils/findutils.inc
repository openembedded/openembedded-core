DESCRIPTION = "find, locate, and xargs binaries."
SECTION = "console/utils"
LICENSE = "GPL"

SRC_URI = "${GNU_MIRROR}/findutils/findutils-${PV}.tar.gz"

inherit autotools gettext

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_linux-uclibc = "--without-included-regex"
