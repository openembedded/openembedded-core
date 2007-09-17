LICENSE = "LGPL"
SECTION = "x11/libs"
PR = "r0"
DESCRIPTION = "GNOME Accessibility Implementation Library"
DEPENDS = "gtk+"

inherit gnome

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/gtk-2.0/modules/*.so"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"

do_stage() {
	gnome_stage_includes
	oe_libinstall -C gail -so libgail ${STAGING_LIBDIR}
	oe_libinstall -C libgail-util -so libgailutil ${STAGING_LIBDIR}
}
