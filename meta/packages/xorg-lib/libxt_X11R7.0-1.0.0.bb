require xorg-lib-common.inc

DESCRIPTION = "X Toolkit Intrinsics"
PRIORITY = "optional"

DEPENDS += " libsm virtual/libx11 xproto kbproto"
PROVIDES = "xt"

XORG_PN = "libXt"

EXTRA_OECONF="--enable-malloc0returnsnull --disable-install-makestrs --disable-xkb"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C util 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makestrs
	)
	oe_runmake
}
