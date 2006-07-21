DESCRIPTION = "Freetype font rendering library"
HOMEPAGE = "http://www.freetype.org"
SECTION = "libs"
LICENSE = "freetype"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
	   file://configure.patch;patch=1 \
	   file://no-hardcode.patch;patch=1"
S = "${WORKDIR}/freetype-${PV}"

inherit autotools pkgconfig binconfig

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"

do_configure() {
	cd builds/unix
	gnu-configize
	aclocal -I .
	autoconf
	cd ${S}
	oe_runconf
}

do_stage() {
	autotools_stage_includes
	oe_libinstall -so -a -C objs libfreetype ${STAGING_LIBDIR}
}

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${bindir}"
