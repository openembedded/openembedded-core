DESCRIPTION = "Media deamon and play queue manager"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 dbus-glib gtk+"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage
