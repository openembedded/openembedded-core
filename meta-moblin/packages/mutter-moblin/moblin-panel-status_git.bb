DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "nbtk mutter-moblin dbus-glib clutter-1.0 telepathy-mission-control mojito"
PV = "0.1+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"

FILES_${PN} += "${datadir}/dbus-1/services"

S = "${WORKDIR}/git"

inherit autotools_stage

