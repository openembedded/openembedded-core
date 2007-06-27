DESCRIPTION = "Open Hardware Manager"
HOMEPAGE = "http://freedesktop.org/Software/ohm"
LICENSE = "LGPL"

DEPENDS = "dbus-glib intltool-native hal"
RDEPENDS += "udev hal-info"
SRC_URI = "git://anongit.freedesktop.org/git/ohm/;protocol=git"

PV = "0.0+git${SRCDATE}"
PR = "r0"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--with-distro=debian"
