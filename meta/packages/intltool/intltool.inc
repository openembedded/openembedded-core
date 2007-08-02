DESCRIPTION = "Utility scripts for internationalizing XML"
PRIORITY = "optional"
SECTION = "devel"
LICENSE = "GPL"

URLV="${@'.'.join(bb.data.getVar('PV',d,1).split('.')[0:2])}"
SRC_URI = "${GNOME_MIRROR}/intltool/${URLV}/intltool-${PV}.tar.bz2"
S = "${WORKDIR}/intltool-${PV}"

inherit autotools  pkgconfig

do_stage() {
	install -m 0644 intltool.m4 ${STAGING_DATADIR}/aclocal/
}
