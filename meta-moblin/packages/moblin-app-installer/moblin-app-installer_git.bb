DESCRIPTION = "Moblin Appliction Installer"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://opkg-7f7d50079c8bcc63874fd60a61f23d849e025445.patch;patch=1;rev=7f7d50079c8bcc63874fd60a61f23d849e025445 \
           file://opkg.patch;patch=1;notrev=7f7d50079c8bcc63874fd60a61f23d849e025445"
LICENSE = "GPLv2"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"
DEPENDS = "clutter-1.0 glib-2.0 nbtk libxml2 gtk+ gnome-packagekit opkg"

inherit autotools
