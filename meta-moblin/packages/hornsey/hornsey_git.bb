DESCRIPTION = "The Moblin Media Player"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "clutter clutter-gst bickley bognor-regis nbtk startup-notification libunique"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/icons"

inherit autotools_stage
