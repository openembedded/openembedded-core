require findutils.inc

PR = "r1"

SRC_URI += "file://01-27017.patch \
            file://02-28824.patch \
            file://03-28872.patch \
            file://findutils_fix_for_x32.patch "

SRC_URI[md5sum] = "351cc4adb07d54877fa15f75fb77d39f"
SRC_URI[sha256sum] = "434f32d171cbc0a5e72cfc5372c6fc4cb0e681f8dce566a0de5b6fccd702b62a"

# http://savannah.gnu.org/bugs/?27299
EXTRA_OECONF += "gl_cv_func_wcwidth_works=yes"

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
