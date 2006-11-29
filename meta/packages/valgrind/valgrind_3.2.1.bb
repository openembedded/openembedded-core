DESCRIPTION = "Valgrind memory debugger"
DEPENDS = "virtual/libx11"
HOMEPAGE = "http://www.valgrind.org/"
LICENSE = "GPL"

SRC_URI = "http://www.valgrind.org/downloads/valgrind-${PV}.tar.bz2 \
           file://makefile_fix.patch;patch=1"

S = "${WORKDIR}/valgrind-${PV}"

COMPATIBLE_HOST = 'i.86.*-linux'

inherit autotools

EXTRA_OECONF = "--enable-tls"

FILES = "${bindir}/bin"
