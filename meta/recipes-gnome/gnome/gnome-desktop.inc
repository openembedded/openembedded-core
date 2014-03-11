SUMMARY = "GNOME library for reading .desktop files"
SECTION = "x11/gnome"
LICENSE = "GPLv2 & LGPLv2"
DEPENDS = "gconf libxrandr virtual/libx11 gtk+ glib-2.0 gnome-doc-utils startup-notification"

EXTRA_OECONF = "--disable-scrollkeeper --disable-desktop-docs"

do_configure_prepend () {
	cp ${STAGING_DATADIR_NATIVE}/gnome-common/data/omf.make ${S}
}

FILES_${PN} += "${datadir}/gnome-about ${datadir}/libgnome-desktop/pnp.ids"

PR = "r6"

inherit gnomebase

do_install_append () {
	sed -i -e's,${STAGING_BINDIR_NATIVE},${bindir},g' ${D}${bindir}/gnome-about
	sed -i -e '1s,#!.*python,#! ${USRBINPATH}/env python,' ${D}${bindir}/gnome-about
}


