DESCRIPTION = "Valgrind memory debugger"
DEPENDS = "virtual/libx11"
HOMEPAGE = "http://www.valgrind.org/"
LICENSE = "GPL"
PR = "r2"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://configurefix.patch;patch=1 "

S = "${WORKDIR}/valgrind-${PV}"

COMPATIBLE_HOST = 'i.86.*-linux'

inherit autotools_stage

EXTRA_OECONF = "--enable-tls"

FILES_${PN}-dbg += "${libdir}/${PN}/*/.debug/*"
