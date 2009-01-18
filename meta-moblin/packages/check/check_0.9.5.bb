DESCRIPTION = "a unit test framework for C"
LICENSE = "LGPL"
PRIORITY = "optional"
SECTION = "devel"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/check/check-${PV}.tar.gz \
           file://configure_fix.patch;patch=1"
S = "${WORKDIR}/check-${PV}"

inherit autotools_stage pkgconfig

EXTRA_OECONF += "--enable-plain-docdir"
