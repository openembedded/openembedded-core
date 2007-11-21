require findutils.inc

do_install_append () {
        mv ${D}${bindir}/find ${D}${bindir}/find.${PN}
        mv ${D}${bindir}/xargs ${D}${bindir}/xargs.${PN}
}

pkg_postinst_${PN} () {
	for i in find xargs; do update-alternatives --install ${bindir}/$i $i $i.${PN} 100; done
}

pkg_prerm_${PN} () {
	for i in find xargs; do update-alternatives --remove $i $i.${PN}; done
}
