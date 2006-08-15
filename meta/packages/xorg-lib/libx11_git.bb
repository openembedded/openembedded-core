DEFAULT_PREFERENCE = "-1"

DESCRIPTION = "Xlib/XCB: Xlib with XCB transport"
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "XFree86"

DEPENDS = "libxcb xproto xextproto libxau xtrans libxdmcp xcmiscproto xf86bigfontproto kbproto inputproto bigreqsproto util-macros"
PROVIDES = "virtual/libx11"
RPROVIDES = "virtual/libx11"

FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB"
FILES_${PN}-locale += "${datadir}/X11/locale"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libX11;protocol=git"
S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF="--enable-malloc0returnsnull"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C src/util 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makekeys
	)
	rm -f ${STAGING_INCDIR}/X11/Xlib.h
	oe_runmake
}

do_stage() {
	autotools_stage_all
}
