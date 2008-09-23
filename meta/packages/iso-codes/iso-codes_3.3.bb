SECTION = "libs"
DESCRIPTION = "ISO language, territory, currency, script codes and their translations"
PRIORITY = "optional"
LICENSE = "LGPL"
SECTION = "libs"
PACKAGE_ARCH = "all"

SRC_URI = "ftp://pkg-isocodes.alioth.debian.org/pub/pkg-isocodes/iso-codes-${PV}.tar.gz"

inherit autotools

FILES_${PN}-dev="${datadir}/pkgconfig/iso-codes.pc"
FILES_${PN}="${datadir}/xml/iso-codes/ \
             ${datadir}/iso-codes/"

do_stage() {
        autotools_stage_all
}

