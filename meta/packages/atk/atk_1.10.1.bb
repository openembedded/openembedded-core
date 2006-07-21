DEPENDS = "glib-2.0 gtk-doc"
DESCRIPTION = "An accessibility toolkit for GNOME."
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Philip Blundell <pb@handhelds.org>"
LICENSE = "LGPL"

SRC_URI = "ftp://ftp.gtk.org/pub/gtk/v2.8/atk-${PV}.tar.bz2"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-glibtest"

CFLAGS_append = " -I${STAGING_INCDIR}/glib-2.0 \
		  -I${STAGING_INCDIR}/glib-2.0/glib \
		  -I${STAGING_INCDIR}/glib-2.0/gobject"

do_stage () {
	oe_libinstall -so -C atk libatk-1.0 ${STAGING_LIBDIR}
	autotools_stage_includes
}
