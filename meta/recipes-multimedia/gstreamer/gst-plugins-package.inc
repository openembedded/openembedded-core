LIBV = "0.10"

python populate_packages_prepend () {
	gst_libdir = bb.data.expand('${libdir}/gstreamer-${LIBV}', d)
	postinst = d.getVar('plugin_postinst', True)
	glibdir = d.getVar('libdir', True)

	do_split_packages(d, glibdir, '^lib(.*)\.so\.*', 'lib%s', 'gstreamer %s library', extra_depends='', allow_links=True)
	do_split_packages(d, gst_libdir, 'libgst(.*)\.so$', bb.data.expand('${PN}-%s', d), 'GStreamer plugin for %s', postinst=postinst, extra_depends=bb.data.expand('${PN}',d))
	do_split_packages(d, gst_libdir, 'libgst(.*)\.la$', bb.data.expand('${PN}-%s-dev', d), 'GStreamer plugin for %s (development files)', extra_depends=bb.data.expand('${PN}-dev',d))
	do_split_packages(d, gst_libdir, 'libgst(.*)\.a$', bb.data.expand('${PN}-%s-staticdev', d), 'GStreamer plugin for %s (static development files)', extra_depends=bb.data.expand('${PN}-staticdev',d))
	
	pn = d.getVar('PN', 1)
	metapkg =  pn + '-meta'
	d.setVar('ALLOW_EMPTY_' + metapkg, "1")
	d.setVar('FILES_' + metapkg, "")
	blacklist = [ pn + '-locale', pn + '-dev', pn + '-dbg', pn + '-doc' ]
	metapkg_rdepends = []
	packages = d.getVar('PACKAGES', 1).split()
	for pkg in packages[1:]:
		if not pkg in blacklist and not pkg in metapkg_rdepends and not pkg.endswith('-dev') and not pkg.endswith('-dbg') and not pkg.count('locale') and not pkg.count('-staticdev'):
			metapkg_rdepends.append(pkg)
	d.setVar('RDEPENDS_' + metapkg, ' '.join(metapkg_rdepends))
	d.setVar('DESCRIPTION_' + metapkg, pn + ' meta package')
}

ALLOW_EMPTY = "1"

PACKAGES += "${PN}-apps ${PN}-meta ${PN}-glib"
FILES_${PN}-apps = "${bindir}"

FILES_${PN} = "${datadir}/gstreamer-${LIBV}"
FILES_${PN}-dbg += "${libdir}/gstreamer-${LIBV}/.debug"
FILES_${PN}-glib = "${datadir}/glib-2.0"

