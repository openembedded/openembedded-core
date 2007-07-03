require xorg-lib-common.inc

SRC_URI = "${XORG_MIRROR}/individual/lib/libXft-2.1.12.tar.bz2" 
S = "${WORKDIR}/${XORG_PN}-2.1.12"


DESCRIPTION = "X FreeType library. Client-side fonts with FreeType."

DEPENDS += " libxrender freetype fontconfig"
PROVIDES = "xft"
PE = "1"

XORG_PN = "libXft"

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*.so ${libdir}/*.la \
		${libdir}/*.a ${libdir}/pkgconfig \
		${datadir}/aclocal ${bindir} ${sbindir}"

python do_package() {
        if bb.data.getVar('DEBIAN_NAMES', d, 1):
            bb.data.setVar('PKG_${PN}', 'libxft2', d)
        bb.build.exec_func('package_do_package', d)
}
