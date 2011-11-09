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
	packages = d.getVar('PACKAGES', 1).split()
	pkgdest =  d.getVar('PKGDEST', 1)
	
	for pkg in packages:
		icon_dir = '%s/%s/%s/icons' % (pkgdest, pkg, d.getVar('datadir', 1))
		if not os.path.exists(icon_dir):
			continue

		bb.note("adding hicolor-icon-theme dependency to %s" % pkg)	
		rdepends = d.getVar('RDEPENDS_%s' % pkg, 1)
		rdepends = rdepends + ' ' + d.getVar('MLPREFIX') + "hicolor-icon-theme"
		d.setVar('RDEPENDS_%s' % pkg, rdepends)
	
		bb.note("adding gtk-icon-cache postinst and postrm scripts to %s" % pkg)
		
		postinst = d.getVar('pkg_postinst_%s' % pkg, 1) or d.getVar('pkg_postinst', 1)
		if not postinst:
			postinst = '#!/bin/sh\n'
		postinst += d.getVar('gtk_icon_cache_postinst', 1)
		d.setVar('pkg_postinst_%s' % pkg, postinst)

		postrm = d.getVar('pkg_postrm_%s' % pkg, 1) or d.getVar('pkg_postrm', 1)
		if not postrm:
			postrm = '#!/bin/sh\n'
		postrm += d.getVar('gtk_icon_cache_postrm', 1)
		d.setVar('pkg_postrm_%s' % pkg, postrm)
}

