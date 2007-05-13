SECTION = "devel"
DESCRIPTION = "Utility scripts for internationalizing XML"
DEPENDS = "libxml-parser-perl"
LICENSE = "GPL"

SRC_URI = "${GNOME_MIRROR}/intltool/0.35/intltool-${PV}.tar.bz2"
S = "${WORKDIR}/intltool-${PV}"

inherit autotools  pkgconfig

do_stage() {
	install -m 0644 intltool.m4 ${STAGING_DATADIR}/aclocal/
}
