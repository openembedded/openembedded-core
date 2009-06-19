DESCRIPTION = "Glib bindings to connman"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

DEPENDS = "connman glib-2.0 dbus-glib gtk+"

inherit autotools_stage
