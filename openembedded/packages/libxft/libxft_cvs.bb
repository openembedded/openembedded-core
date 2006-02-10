PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT-X"
SECTION = "x11/libs"
DEPENDS = "x11 xproto libxrender freetype fontconfig"
DESCRIPTION = "X FreeType library. Client-side fonts with FreeType."
PROVIDES = "xft"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xft"
S = "${WORKDIR}/Xft"

FILES_${PN} = ${libdir}/lib*.so.*
FILES_${PN}-dev = ${includedir} ${libdir}/lib*.so ${libdir}/*.la \
		${libdir}/*.a ${libdir}/pkgconfig \
		${datadir}/aclocal ${bindir} ${sbindir}

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}

python do_package() {
        if bb.data.getVar('DEBIAN_NAMES', d, 1):
            bb.data.setVar('PKG_${PN}', 'libxft2', d)
        bb.build.exec_func('package_do_package', d)
}
