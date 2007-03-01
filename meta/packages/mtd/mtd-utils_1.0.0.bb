DESCRIPTION = "Tools for managing memory technology devices."
SECTION = "base"
DEPENDS = "zlib"
HOMEPAGE = "http://www.linux-mtd.infradead.org/"
LICENSE = "GPLv2"
PR = "r0"

# RP/kergoth: Builds seem to break with recent mtd-utils
# Can't pin down the exact problems
DEFAULT_PREFERENCE = "-1"

SRC_URI = "ftp://ftp.infradead.org/pub/mtd-utils/mtd-utils-1.0.0.tar.gz"
S = "${WORKDIR}/mtd-utils-${PV}"

CFLAGS_prepend = "-I${WORKDIR}/mtd-utils-${PV}/include "

do_install() {
	oe_runmake install DESTDIR=${D}
}
