DESCRIPTION = "Base X libs."
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "XFree86"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11"

DEPENDS = "xproto-native xextproto-native libxau-native xtrans-native libxdmcp-native xcmiscproto-native xf86bigfontproto-native kbproto-native inputproto-native bigreqsproto-native util-macros-native"
PROVIDES = "x11-native"

SRC_URI = "${XORG_MIRROR}/X11R7.1/src/lib/libX11-${PV}.tar.bz2"
S = "${WORKDIR}/libX11-${PV}"

inherit native autotools pkgconfig

EXTRA_OECONF="--enable-malloc0returnsnull"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C src/util 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makekeys
	)
	rm -f ${STAGING_INCDIR}/X11/Xlib.h
	oe_runmake
}
