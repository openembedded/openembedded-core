DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
LICENSE = "libpng"
SECTION = "libs"
PRIORITY = "required"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"

DEPENDS = "zlib"

PACKAGES =+ "${PN}12 ${PN}12-dev"

FILES_${PN}12 = ${libdir}/libpng12.so.*
FILES_${PN}12-dev = ${libdir}/libpng12.* ${includedir}/libpng12 ${libdir}/pkgconfig/libpng12.pc
FILES_${PN} = ${libdir}/lib*.so.*
FILES_${PN}-dev = ${includedir} ${libdir}/lib*.so ${libdir}/*.la \
		${libdir}/*.a ${libdir}/pkgconfig \
		${datadir}/aclocal ${bindir} ${sbindir}

SRC_URI = "${SOURCEFORGE_MIRROR}/libpng/libpng-${PV}.tar.bz2"
S = "${WORKDIR}/libpng-${PV}"

inherit pkgconfig binconfig

EXTRA_OEMAKE_append = " ZLIBINC=${STAGING_INCDIR} ZLIBLIB=${STAGING_LIBDIR}"

do_compile() {
	sed < scripts/makefile.linux > makefile -e 's/^ZLIBINC.*//' -e 's/^ZLIBLIB.*//'
	unset LDFLAGS
	oe_runmake 'CC=${CC}' 'LD=${LD}' 'CFLAGS=${CFLAGS}' \
		   'ZLIBINC=${STAGING_INCDIR}' \
		   'ZLIBLIB=${STAGING_LIBDIR}'
}

do_stage() {
	cp libpng.pc libpng12.pc
	install -m 644 png.h ${STAGING_INCDIR}/png.h
	install -m 644 pngconf.h ${STAGING_INCDIR}/pngconf.h
	oe_libinstall -so libpng12 ${STAGING_LIBDIR}/
	ln -sf libpng12.so ${STAGING_LIBDIR}/libpng.so
}

do_install() {
	install -d ${D}${bindir} ${D}${mandir} \
		   ${D}${libdir} ${D}${includedir}
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

