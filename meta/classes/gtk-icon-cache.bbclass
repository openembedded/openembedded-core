FILES_${PN} += "${datadir}/icons/hicolor"
RDEPENDS += "hicolor-icon-theme"

# This could run on the host as icon cache files are architecture independent,
# but there is no gtk-update-icon-cache built natively.
gtk-icon-cache_postinst() {
if [ "x$D" != "x" ]; then
        exit 1
fi
gtk-update-icon-cache -q /usr/share/icons/hicolor
}

gtk-icon-cache_postrm() {
gtk-update-icon-cache -q /usr/share/icons/hicolor
}

python populate_packages_append () {
	import os.path
	packages = bb.data.getVar('PACKAGES', d, 1).split()
	workdir = bb.data.getVar('WORKDIR', d, 1)
	
	for pkg in packages:
		icon_dir = '%s/install/%s/%s/icons/hicolor' % (workdir, pkg, bb.data.getVar('datadir', d, 1))
		if not os.path.exists(icon_dir):
			continue
		
		bb.note("adding gtk-icon-cache postinst and postrm scripts to %s" % pkg)
		
		postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1) or bb.data.getVar('pkg_postinst', d, 1)
		if not postinst:
			postinst = '#!/bin/sh\n'
		postinst += bb.data.getVar('gtk-icon-cache_postinst', d, 1)
		bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

		postrm = bb.data.getVar('pkg_postrm_%s' % pkg, d, 1) or bb.data.getVar('pkg_postrm', d, 1)
		if not postrm:
			postrm = '#!/bin/sh\n'
		postrm += bb.data.getVar('gtk-icon-cache_postrm', d, 1)
		bb.data.setVar('pkg_postrm_%s' % pkg, postrm, d)
}

