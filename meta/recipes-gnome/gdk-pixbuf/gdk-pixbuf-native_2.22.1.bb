require gdk-pixbuf.inc
inherit native

DEPENDS = "libpng-native gettext-native glib-2.0-native"
PR = "r0"

PACKAGES_DYNAMIC = ""

do_install_append() {
	GDK_PIXBUF_MODULEDIR=${D}${libdir}/gdk-pixbuf-2.0/2.10.0/loaders ${D}${bindir}/gdk-pixbuf-query-loaders > ${D}${libdir}/gdk-pixbuf-2.0/2.10.0/loaders.cache
	sed -i -e 's#${D}##g' ${D}${libdir}/gdk-pixbuf-2.0/2.10.0/loaders.cache
	find ${D}${libdir} -name "libpixbufloader-*.la" -exec rm \{\} \;
}
