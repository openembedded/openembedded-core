SUMMARY  = "Check - unit testing framework for C code"
HOMEPAGE = "http://check.sourceforge.net/"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
          "

SRC_URI[md5sum] = "fd5a03979bcab9fb80ba005b55f54178"
SRC_URI[sha256sum] = "ea4e8c7ffb00bb4ffb3f59f11744a71f1cc4212c79f3083c7d9a4b0953976936"

S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig

CACHED_CONFIGUREVARS += "ac_cv_path_AWK_PATH=${bindir}/gawk"

RREPLACES_${PN} = "check (<= 0.9.5)"
RDEPENDS_${PN} += "gawk"
RDEPENDS_${PN}_class-native = ""

BBCLASSEXTEND = "native"
