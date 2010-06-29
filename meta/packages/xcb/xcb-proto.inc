DESCRIPTION = "XCB: an X protocol C binding"
HOMEPAGE = "http://xcb.freedesktop.org"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=XCB"

SECTION = "x11/libs"
LICENSE = "MIT-X"

SRC_URI = "http://xcb.freedesktop.org/dist/xcb-proto-${PV}.tar.bz2"

inherit autotools pkgconfig

FILES_${PN}-dev += "${datadir}/xcb/*.xml"

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"

DEPENDS_append_virtclass-native = " python-native"
BBCLASSEXTEND = "native"
