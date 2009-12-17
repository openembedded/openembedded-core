DESCRIPTION = "GNU dbm is a set of database routines that use extensible hashing."
HOMEPAGE = "http://www.gnu.org/software/gdbm/gdbm.html"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL"
DEPENDS = "gettext"
PR = "r3"

SRC_URI = "${GNU_MIRROR}/gdbm/gdbm-${PV}.tar.gz \
	   file://makefile.patch;patch=1 \
           file://libtool-mode.patch;patch=1"

inherit autotools

BBCLASSEXTEND = "native"
