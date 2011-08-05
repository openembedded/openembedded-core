FILES_${PN} += "${datadir}/icons/hicolor"

DEPENDS += "${@['hicolor-icon-theme', '']['${BPN}' == 'hicolor-icon-theme']}"

# This could run on the host as icon cache files are architecture independent,
# but there is no gtk-update-icon-cache built natively.
gtk_icon_cache_postinst() {
if [ "x$D" != "x" ]; then
        exit 1
fi

# Update the pixbuf loaders in case they haven't been registered yet
GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/2.10.0/loaders gdk-pixbuf-query-loaders --update-cache

for icondir in /usr/share/icons/* ; do
    if [ -d $icondir ] ; then
        gtk-update-icon-cache -qt  $icondir
    fi
done
}

gtk_icon_cache_postrm() {
for icondir in /usr/share/icons/* ; do
    if [ -d $icondir ] ; then
        gtk-update-icon-cache -qt  $icondir
    fi
done
}

python populate_packages_append () {
	packages = bb.data.getVar('PACKAGES', d, 1).split()
	pkgdest =  bb.data.getVar('PKGDEST', d, 1)
	
	for pkg in packages:
		icon_dir = '%s/%s/%s/icons' % (pkgdest, pkg, bb.data.getVar('datadir', d, 1))
		if not os.path.exists(icon_dir):
			continue

		bb.note("adding hicolor-icon-theme dependency to %s" % pkg)	
		rdepends = bb.data.getVar('RDEPENDS_%s' % pkg, d, 1)
		rdepends += " hicolor-icon-theme"
		bb.data.setVar('RDEPENDS_%s' % pkg, rdepends, d)
	
		bb.note("adding gtk-icon-cache postinst and postrm scripts to %s" % pkg)
		
		postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1) or bb.data.getVar('pkg_postinst', d, 1)
		if not postinst:
			postinst = '#!/bin/sh\n'
		postinst += bb.data.getVar('gtk_icon_cache_postinst', d, 1)
		bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

		postrm = bb.data.getVar('pkg_postrm_%s' % pkg, d, 1) or bb.data.getVar('pkg_postrm', d, 1)
		if not postrm:
			postrm = '#!/bin/sh\n'
		postrm += bb.data.getVar('gtk_icon_cache_postrm', d, 1)
		bb.data.setVar('pkg_postrm_%s' % pkg, postrm, d)
}

