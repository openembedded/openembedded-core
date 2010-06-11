DESCRIPTION = "GNU dbm is a set of database routines that use extensible hashing."
HOMEPAGE = "http://www.gnu.org/software/gdbm/"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d8e20eece214df8ef953ed5857862150"

PR = "r4"

SRC_URI = "${GNU_MIRROR}/gdbm/gdbm-${PV}.tar.gz \
	   file://makefile.patch;patch=1 \
           file://libtool-mode.patch;patch=1"

inherit autotools

BBCLASSEXTEND = "native"
