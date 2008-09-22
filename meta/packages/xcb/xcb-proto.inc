DESCRIPTION = "XCB: an X protocol C binding"
SECTION = "x11/libs"
LICENSE = "MIT-X"
HOMEPAGE = "http://xcb.freedesktop.org"

SRC_URI = "http://xcb.freedesktop.org/dist/xcb-proto-${PV}.tar.bz2"

inherit autotools pkgconfig

FILES_${PN}-dev += "${datadir}/xcb/*.xml"

do_stage() {
	oe_runmake DESTDIR="${D}" install
	mkdir -p ${STAGING_LIBDIR}/pkgconfig
	install ${D}${libdir}/pkgconfig/xcb-proto.pc ${STAGING_LIBDIR}/pkgconfig
	mkdir -p ${STAGING_DATADIR}/xcb
	install ${D}${datadir}/xcb/*.xml ${STAGING_DATADIR}/xcb
}

