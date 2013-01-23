DESCRIPTION  = "Check Test Framework"
HOMEPAGE = "http://check.sourceforge.net/"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
           file://libcheck_fix_for_automake-1.12.patch \
          "

SRC_URI[md5sum] = "f3702f2fcfc19ce3f62dca66c241a168"
SRC_URI[sha256sum] = "1a7a9abb9d051e1b9da4149ce651436a29e20135a40bdb202bd7b2bef3878ac9"

S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig

CACHED_CONFIGUREVARS += "ac_cv_path_AWK_PATH=${bindir}/gawk"

RREPLACES_${PN} = "check (<= 0.9.5)"
RDEPENDS_${PN} += "gawk"
