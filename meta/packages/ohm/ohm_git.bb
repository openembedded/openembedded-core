DESCRIPTION = "Open Hardware Manager"
HOMEPAGE = "http://freedesktop.org/Software/ohm"
LICENSE = "LGPL"

DEPENDS = "dbus-glib intltool-native hal"
RDEPENDS += "udev hal-info"
SRC_URI = "git://anongit.freedesktop.org/git/ohm/;protocol=git"

PV = "0.0+git${SRCDATE}"
PR = "r2"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_configure_prepend() {
        touch gtk-doc.make
}

EXTRA_OECONF = "--with-distro=debian --disable-gtk-doc --with-xauth-dir=/home/root"
