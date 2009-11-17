require libtool.inc

PR = "r14"

SRC_URI = "${GNU_MIRROR}/libtool/libtool-${PV}a.tar.gz \
           file://dolt.m4"

PACKAGES =+ "libltdl libltdl-dev libltdl-dbg"
FILES_${PN} += "${datadir}/aclocal*"
FILES_libltdl = "${libdir}/libltdl.so.*"
FILES_libltdl-dev = "${libdir}/libltdl.* ${includedir}/ltdl.h"
FILES_libltdl-dbg = "${libdir}/.debug/"

inherit autotools_stage

EXTRA_AUTORECONF = "--exclude=libtoolize"
