SECTION = "x11/libs"
LICENSE = "MIT-X"
DEPENDS = "x11 xproto libxrender freetype fontconfig"
DESCRIPTION = "X FreeType library. Client-side fonts with FreeType."
PROVIDES = "xft"
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXft-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXft-${PV}"

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
