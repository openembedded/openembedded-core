DESCRIPTION  = "Check Test Framework"
HOMEPAGE = "http://check.sourceforge.net/"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
           file://libcheck_fix_for_automake-1.12.patch \
          "

SRC_URI[md5sum] = "6d10a8efb9a683467b92b3bce97aeb30"
SRC_URI[sha256sum] = "823819235753e94ae0bcab3c46cc209de166c32ff2f52cefe120597db4403e6d"

S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig

CACHED_CONFIGUREVARS += "ac_cv_path_AWK_PATH=${bindir}/gawk"

RREPLACES_${PN} = "check (<= 0.9.5)"
RDEPENDS_${PN} += "gawk"
