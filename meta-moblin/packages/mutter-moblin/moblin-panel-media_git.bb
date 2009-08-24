DESCRIPTION = "Moblin Applications Panel Plugin"
SECTION = "x11/wm"
LICENSE = "GPLv2"
DEPENDS = "mutter-moblin bickley bognor-regis gtk+"
PV = "0.1+git${SRCPV}"
PR = "r2"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"

FILES_${PN} += "${datadir}/dbus-1/services"

S = "${WORKDIR}/git"

inherit autotools_stage

