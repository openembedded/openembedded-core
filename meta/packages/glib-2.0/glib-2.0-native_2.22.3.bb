DESCRIPTION = "GLib is a general-purpose utility library, \
which provides many useful data types, macros, \
type conversions, string utilities, file utilities, a main \
loop abstraction, and so on. It works on many \
UNIX-like platforms, Windows, OS/2 and BeOS."
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS += "gtk-doc-native"
PR = "r1"

SRC_URI = "http://download.gnome.org/sources/glib/2.22/glib-${PV}.tar.bz2 \
           file://glib-gettextize-dir.patch;patch=1 \
           file://configure-libtool.patch;patch=1 \
           file://glibconfig-sysdefs.h"

S = "${WORKDIR}/glib-${PV}"

inherit autotools_stage pkgconfig native gettext

acpaths = ""
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h .
}

do_install_append () {
	install -d ${D}${includedir}/glib-2.0/glib
	install -m 0755 ${S}/glibconfig.h ${D}${includedir}/glib-2.0/glibconfig.h
}
