LICENSE = "GPL"
SECTION = "x11/gnome"

PR = "r1"

inherit autotools gnome pkgconfig

DEPENDS = "gtk+ libgcrypt libtasn1 libtasn1-native"

EXTRA_OECONF = "--disable-gtk-doc"

SRC_URI += "file://org.gnome.keyring.service"

do_install_append () {
	install -d ${D}${datadir}/dbus-1/services
	install -m 0644 ${WORKDIR}/org.gnome.keyring.service ${D}${datadir}/dbus-1/services
}

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/gnome-keyring/standalone/.debug/"
FILES_${PN}-dbg += "${libdir}/gnome-keyring/devel/.debug/"
