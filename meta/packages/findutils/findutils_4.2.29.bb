require findutils.inc

LICENSE = "GPLv2+"
PR = "r2"

do_install_append () {
        if [ -e ${D}${bindir}/find ]; then
            mv ${D}${bindir}/find ${D}${bindir}/find.${PN}
            mv ${D}${bindir}/xargs ${D}${bindir}/xargs.${PN}
        fi
}

pkg_postinst_${PN} () {
	for i in find xargs; do update-alternatives --install ${bindir}/$i $i $i.${PN} 100; done
}

pkg_prerm_${PN} () {
	for i in find xargs; do update-alternatives --remove $i $i.${PN}; done
}

BBCLASSEXTEND = "native"
