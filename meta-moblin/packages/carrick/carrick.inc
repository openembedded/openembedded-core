DESCIPTION = "Connection Management Panel Applet"
SRC_URI = "http://download.moblin.org/sources/carrick/1.1/carrick-${PV}.tar.bz2"
LICENSE = "GPLv2"

DEPENDS = "nbtk mutter-moblin librest libnotify mobile-broadband-provider-info"
RDEPENDS = "connman"

FILES_${PN} += "${datadir}/icons/hicolor"

inherit autotools_stage

do_configure_prepend () {
	mkdir -p ${S}/build/autotools
}
