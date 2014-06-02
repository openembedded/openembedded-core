SUMMARY  = "Check - unit testing framework for C code"
HOMEPAGE = "http://check.sourceforge.net/"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
          "

SRC_URI[md5sum] = "95530868f81a9496b2518fd2b713008a"
SRC_URI[sha256sum] = "ca6589c34f9c60ffd4c3e198ce581e944a9f040ca9352ed54068dd61bebb5cb7"

S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig texinfo

CACHED_CONFIGUREVARS += "ac_cv_path_AWK_PATH=${bindir}/gawk"

RREPLACES_${PN} = "check (<= 0.9.5)"
RDEPENDS_${PN} += "gawk"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
