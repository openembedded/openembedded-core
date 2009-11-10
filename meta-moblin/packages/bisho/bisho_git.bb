DESCRIPTION = "Web Services Settings"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r0"

DEPENDS = "gtk+ mojito nbtk"

S = "${WORKDIR}/git"

inherit autotools_stage

FILES_${PN} += "${datadir}/icons/"
