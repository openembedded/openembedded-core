DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
LICENSE = "libpng"
SECTION = "libs"
DEPENDS = "zlib gettext"
PRIORITY = "required"
PR = "r8"

SRC_URI = "${SOURCEFORGE_MIRROR}/libpng/libpng-${PV}.tar.bz2 \
           file://makefile_fix.patch;patch=1"

inherit autotools binconfig pkgconfig pkgconfig_stage

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${mandir}
	install -d ${D}${libdir}
	install -d ${D}${includedir}
	unset LDFLAGS
	oe_runmake 'prefix=${prefix}' 'DESTDIR=${D}' \
		   'DB=${D}${bindir}' 'DI=${D}${includedir}' \
		   'DL=${D}${libdir}' 'DM=${D}${mandir}' \
		   install
}

python do_package() {
        if bb.data.getVar('DEBIAN_NAMES', d, 1):
            bb.data.setVar('PKG_${PN}', 'libpng12', d)
        bb.build.exec_func('package_do_package', d)
}

PACKAGES =+ "${PN}12-dbg ${PN}12 ${PN}12-dev"

FILES_${PN}12-dbg += "${libdir}/libpng12*.dbg"
FILES_${PN}12 = "${libdir}/libpng12${SOLIBS}"
FILES_${PN}12-dev = "${libdir}/libpng12.* ${includedir}/libpng12 ${libdir}/pkgconfig/libpng12.pc"
FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev = "${includedir} ${libdir}/lib*${SOLIBSDEV} ${libdir}/*.la \
        ${libdir}/*.a ${libdir}/pkgconfig \
        ${datadir}/aclocal ${bindir} ${sbindir}"

BBCLASSEXTEND = "native"
