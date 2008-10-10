require libtool.inc

PR = "r14"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}.tar.gz \
           file://dolt.m4"
S = "${WORKDIR}/libtool-${PV}"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h"
FILES_libltdl-dbg = "${libdir}/.debug/"

inherit autotools

EXTRA_AUTORECONF = "--exclude=libtoolize"

do_stage() {
	install -d ${STAGING_INCDIR}/libltdl
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
	install -m 0644 libltdl/libltdl/*.h ${STAGING_INCDIR}/libltdl/
	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
}
