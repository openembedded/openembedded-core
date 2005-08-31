DESCRIPTION = "GNU dbm is a set of database routines that use extensible hashing."
HOMEPAGE = "http://www.gnu.org/software/gdbm/gdbm.html"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL"

SRC_URI = "${GNU_MIRROR}/gdbm/gdbm-${PV}.tar.gz \
	   file://makefile.patch;patch=1"

inherit autotools 

do_stage () {
	oe_libinstall -so -a libgdbm ${STAGING_LIBDIR}
	install -m 0644 ${S}/gdbm.h ${STAGING_INCDIR}/
}
