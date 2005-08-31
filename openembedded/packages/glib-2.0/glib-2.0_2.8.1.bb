DESCRIPTION = "GLib is a general-purpose utility library, \
which provides many useful data types, macros, \
type conversions, string utilities, file utilities, a main \
loop abstraction, and so on. It works on many \
UNIX-like platforms, Windows, OS/2 and BeOS."
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Philip Blundell <pb@handhelds.org>"
DEPENDS += "glib-2.0-native gtk-doc"
DEPENDS += "virtual/libiconv virtual/libintl"
PACKAGES =+ "glib-2.0-utils "
PR = "r0"

LEAD_SONAME = "libglib-2.0.*"
FILES_glib-2.0-utils = "${bindir}/*"

EXTRA_OECONF = "--disable-debug"

SRC_URI = "ftp://ftp.gtk.org/pub/gtk/v2.8/glib-${PV}.tar.bz2 \
           file://glibconfig-sysdefs.h"

S = "${WORKDIR}/glib-${PV}"

inherit autotools pkgconfig gettext

python () {
	if bb.data.getVar("USE_NLS", d, 1) == "no":
		raise bb.parse.SkipPackage("${PN} requires native language support.")
}

acpaths = ""
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h .
}

do_stage () {
	oe_libinstall -so -C glib libglib-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C gmodule libgmodule-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C gthread libgthread-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C gobject libgobject-2.0 ${STAGING_LIBDIR}
	autotools_stage_includes
	install -d ${STAGING_INCDIR}/glib-2.0/glib
	install -m 0755 ${S}/glibconfig.h ${STAGING_INCDIR}/glib-2.0/glibconfig.h
	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 ${S}/m4macros/glib-2.0.m4 ${STAGING_DATADIR}/aclocal/glib-2.0.m4
	install -m 0644 ${S}/m4macros/glib-gettext.m4 ${STAGING_DATADIR}/aclocal/glib-gettext.m4
}
