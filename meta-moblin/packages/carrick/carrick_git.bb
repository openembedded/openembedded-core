DESCIPTION = "Connection Management Panel Applet"
SRC_URI = "git://git.moblin.org/${PN}-ng.git;protocol=git"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "gconnman nbtk mutter-moblin"
RDEPENDS = "connman gconnman"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/icons/hicolor"

inherit autotools_stage

do_configure_prepend () {
	mkdir -p ${S}/build/autotools
}
