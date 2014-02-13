SUMMARY  = "Check - unit testing framework for C code"
HOMEPAGE = "http://check.sourceforge.net/"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
          "

SRC_URI[md5sum] = "46fe540d1a03714c7a1967dbc6d484e7"
SRC_URI[sha256sum] = "c7d47e55e133a0ca19a52e08a99333ac55cb22618b53719b7f4117a1875b1ea3"

S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig

CACHED_CONFIGUREVARS += "ac_cv_path_AWK_PATH=${bindir}/gawk"

RREPLACES_${PN} = "check (<= 0.9.5)"
RDEPENDS_${PN} += "gawk"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
