DESCRIPTION = "find, locate, and xargs binaries."
HOMEPAGE = "http://www.gnu.org/software/findutils/"
BUGTRACKER = "http://savannah.gnu.org/bugs/?group=findutils"
SECTION = "console/utils"

# GPLv2+ (<< 4.2.32), GPLv3+ (>= 4.2.32)
LICENSE = "GPLv3+"

SRC_URI = "${GNU_MIRROR}/findutils/findutils-${PV}.tar.gz \
           file://mkdir.patch;patch=1"

inherit autotools gettext

# diffutils assumes non-glibc compilation with uclibc and
# this causes it to generate its own implementations of
# standard functionality.  regex.c actually breaks compilation
# because it uses __mempcpy, there are other things (TBD:
# see diffutils.mk in buildroot)
EXTRA_OECONF_linux-uclibc = "--without-included-regex"
