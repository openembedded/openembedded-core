DESCRIPTION = "System Information Icons"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r0"

DEPENDS = "glib-2.0 gtk+ pulseaudio libcanberra libnotify nbtk"

S = "${WORKDIR}/git"

inherit autotools_stage
