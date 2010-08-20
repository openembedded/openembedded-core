DESCRIPTION  = "Check Test Framework"
HOMEPAGE = "http://gitorious.org/opensuse/zypper"
PRIORITY = "optional"
SECTION = "devel"

LICENSE  = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=2d5025d4aa3495befef8f17206a5b0a1"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz"
S = "${WORKDIR}/check-${PV}"

inherit autotools pkgconfig

RREPLACES_${PN} = "check (<= 0.9.5)"
