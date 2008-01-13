DESCRIPTION = "GLib is a general-purpose utility library, \
which provides many useful data types, macros, \
type conversions, string utilities, file utilities, a main \
loop abstraction, and so on. It works on many \
UNIX-like platforms, Windows, OS/2 and BeOS."
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS += "glib-2.0-native gtk-doc-native"
DEPENDS += "virtual/libiconv virtual/libintl"
PACKAGES =+ "glib-2.0-utils "

LEAD_SONAME = "libglib-2.0.*"
FILES_glib-2.0-utils = "${bindir}/*"

EXTRA_OECONF = "--enable-included-printf=no"

S = "${WORKDIR}/glib-${PV}"

inherit autotools pkgconfig gettext

require glib-2.0.inc

acpaths = ""
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h .
}

do_stage () {
	autotools_stage_all
	install -d ${STAGING_INCDIR}/glib-2.0/glib
	install -m 0755 ${S}/glibconfig.h ${STAGING_INCDIR}/glib-2.0/glibconfig.h
}

FILES_${PN} = "${libdir}/lib*so.*"
FILES_${PN}-dev += "${libdir}/glib-2.0/include"

ARM_INSTRUCTION_SET = "arm"
