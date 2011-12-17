DESCRIPTION = "GNOME configuration system"
SECTION = "x11/gnome"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

DEPENDS = "glib-2.0 dbus dbus-glib libxml2 intltool-native polkit"
DEPENDS_virtclass-native = "glib-2.0-native dbus-native dbus-glib-native libxml2-native intltool-native gnome-common-native"

PR = "r3"

inherit gnomebase

SRC_URI = "${GNOME_MIRROR}/GConf/${@gnome_verdir("${PV}")}/GConf-${PV}.tar.bz2;name=archive \
	   file://nointro.patch \
	   file://backenddir.patch"

SRC_URI[archive.md5sum] = "f80329173cd9d134ad07e36002dd2a15"
SRC_URI[archive.sha256sum] = "52008a82a847527877d9e1e549a351c86cc53cada4733b8a70a1123925d6aff4"

S = "${WORKDIR}/GConf-${PV}"

POLKIT_OECONF = "--enable-defaults-service"
POLKIT_OECONF_virtclass-native = "--disable-defaults-service"
EXTRA_OECONF = "--disable-gtk-doc --disable-gtk --enable-shared --disable-static --enable-debug=yes \
                --disable-introspection --disable-orbit --with-openldap=no ${POLKIT_OECONF}"

do_configure_prepend () {
	touch gtk-doc.make
}

do_install_append() {
	# this directory need to be created to avoid an Error 256 at gdm launch
	install -d ${D}${sysconfdir}/gconf/gconf.xml.system

	# this stuff is unusable
	rm ${D}${libdir}/GConf/*/*.*a
	rm ${D}${libdir}/gio/*/*.*a
}

# disable dbus-x11 when x11 isn't in DISTRO_FEATURES
RDEPENDS_${PN} += "${@base_contains('DISTRO_FEATURES', 'x11', 'dbus-x11', '', d)}"
RDEPENDS_${PN}_virtclass-native = ""

FILES_${PN} += "${libdir}/GConf/* \
	        ${libdir}/gio/*/*.so \
		${datadir}/polkit* \
	       	${datadir}/dbus-1/services/*.service \
	       	${datadir}/dbus-1/system-services/*.service \
	        "
FILES_${PN}-dbg += "${libdir}/*/*/.debug"
FILES_${PN}-dev += "${datadir}/sgml/gconf/gconf-1.0.dtd"

BBCLASSEXTEND = "native"
