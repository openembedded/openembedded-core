require moblin-panel-myzone.inc

DEPENDS += "libical"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0.12+git${SRCPV}"
S = "${WORKDIR}/git"


