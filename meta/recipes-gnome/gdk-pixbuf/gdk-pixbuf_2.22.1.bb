require gdk-pixbuf.inc

PR = "r0"

FILES_${PN} = "${bindir}/gdk-pixbuf-query-loaders \
	${libdir}/lib*.so.*"

FILES_${PN}-dev += " \
	${bindir}/gdk-pixbuf-csource \
	${includedir}/*"

FILES_${PN}-dbg += " \
        ${libdir}/.debug/* \
	${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders/.debug/*"

postinst_pixbufloader () {
if [ "x$D" != "x" ]; then
    exit 1
fi

GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders gdk-pixbuf-query-loaders --update-cache

test -x ${bindir}/gtk-update-icon-cache && gtk-update-icon-cache  -q ${datadir}/icons/hicolor
}

PACKAGES_DYNAMIC += "gdk-pixbuf-loader-*"

python populate_packages_prepend () {
	postinst_pixbufloader = bb.data.getVar("postinst_pixbufloader", d, 1)

	loaders_root = bb.data.expand('${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders', d)

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', postinst_pixbufloader)
}

