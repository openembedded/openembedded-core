DESCRIPTION = "gThumb is an image viewer and browser for the GNOME Desktop"
SECTION = "x11/gnome"
LICENSE = "GPL"
DEPENDS = "glib-2.0 gtk+ libbonobo libgnome libgnomeui gnome-vfs libxml2 libglade gnome-doc-utils"

#EXTRA_OECONF = "--disable-scrollkeeper"

inherit gnome pkgconfig

do_stage () {
	autotools_stage_all
}

FILES_${PN} += "${datadir}/icons"
FILES_${PN}-dbg += "${libdir}/gthumb/modules/.debug"