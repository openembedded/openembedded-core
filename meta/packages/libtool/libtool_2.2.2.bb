require libtool.inc

PR = "r5"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}.tar.gz \
           file://uclibc.patch;patch=1 \
           file://dolt.m4"
S = "${WORKDIR}/libtool-${PV}"

#DEFAULT_PREFERENCE = "-1"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h"
FILES_libltdl-dbg = "${libdir}/.debug/"

inherit autotools

EXTRA_AUTORECONF = "--exclude=libtoolize"

do_stage () {
	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
}
