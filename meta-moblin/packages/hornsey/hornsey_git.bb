DESCRIPTION = "The Moblin Media Player"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r2"

DEPENDS = "clutter-1.0 clutter-gst bickley bognor-regis nbtk startup-notification libunique"

EXTRA_OECONF = "--disable-shave"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/icons"

inherit autotools_stage
