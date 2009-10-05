DESCRIPTION = "Moblin Appliction Installer"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://opkg.patch;patch=1 "
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"
DEPENDS = "clutter-1.0 glib-2.0 nbtk libxml2 gtk+ gnome-packagekit opkg"

inherit autotools_stage
