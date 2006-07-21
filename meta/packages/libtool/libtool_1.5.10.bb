DESCRIPTION = "Generic library support script \
This is GNU libtool, a generic library support script.  Libtool hides \
the complexity of generating special library types (such as shared \
libraries) behind a consistent interface."
HOMEPAGE = "http://www.gnu.org/software/libtool/libtool.html"
LICENSE = "GPL"
SECTION = "devel"
PR = "r3"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}.tar.gz \
           file://autotools.patch;patch=1 \
	   file://uclibc.patch;patch=1 \
	   file://3figures.patch;patch=1" 
S = "${WORKDIR}/libtool-${PV}"

PACKAGES = "libltdl libltdl-dev ${PN}"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h"

inherit autotools

EXTRA_AUTORECONF = "--exclude=libtoolize"

do_configure () {
	find ${S} -name acinclude.m4 | for m4 in `cat`; do
		cat ${S}/libtool.m4 ${S}/ltdl.m4 > $m4
	done
	autotools_do_configure
}

do_stage () {
	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
}
