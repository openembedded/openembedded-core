DESCRIPTION = "GNOME configuration system"
SECTION = "x11/gnome"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

POLKIT = "polkit"
POLKIT_libc-uclibc = ""

DEPENDS = "glib-2.0 dbus dbus-glib libxml2 intltool-native ${POLKIT} gobject-introspection-stub"
DEPENDS_class-native = "glib-2.0-native dbus-native dbus-glib-native libxml2-native intltool-native gnome-common-native gobject-introspection-stub-native"

PR = "r14"

inherit gnomebase gtk-doc

SRC_URI = "${GNOME_MIRROR}/GConf/${@gnome_verdir("${PV}")}/GConf-${PV}.tar.xz;name=archive \
           file://obsolete_automake_macros.patch \
"

S = "${WORKDIR}/GConf-${PV}"

SRC_URI[archive.md5sum] = "1b803eb4f8576c572d072692cf40c9d8"
SRC_URI[archive.sha256sum] = "4ddea9503a212ee126c5b46a0a958fd5484574c3cb6ef2baf38db02e819e58c6"

POLKIT_OECONF = "--enable-defaults-service"
POLKIT_OECONF_class-native = "--disable-defaults-service"
POLKIT_OECONF_libc-uclibc = "--disable-default-service"

EXTRA_OECONF = "--enable-shared --disable-static --enable-debug=yes \
                --disable-introspection --disable-orbit --with-openldap=no ${POLKIT_OECONF} --disable-gtk"

do_install_append() {
	# this directory need to be created to avoid an Error 256 at gdm launch
	install -d ${D}${sysconfdir}/gconf/gconf.xml.system

	# this stuff is unusable
	rm -f ${D}${libdir}/GConf/*/*.*a
	rm -f ${D}${libdir}/gio/*/*.*a
}

do_install_append_class-native() {
	create_wrapper ${D}/${bindir}/gconftool-2 \
		GCONF_BACKEND_DIR=${STAGING_LIBDIR_NATIVE}/GConf/2
}

# disable dbus-x11 when x11 isn't in DISTRO_FEATURES
RDEPENDS_${PN} += "${@base_contains('DISTRO_FEATURES', 'x11', 'dbus-x11', '', d)}"
RDEPENDS_${PN}_class-native = ""

FILES_${PN} += "${libdir}/GConf/* \
                ${libdir}/gio/*/*.so \
                ${datadir}/polkit* \
                ${datadir}/dbus-1/services/*.service \
                ${datadir}/dbus-1/system-services/*.service \
               "
FILES_${PN}-dbg += "${libdir}/*/*/.debug"
FILES_${PN}-dev += "${datadir}/sgml/gconf/gconf-1.0.dtd"

BBCLASSEXTEND = "native"
