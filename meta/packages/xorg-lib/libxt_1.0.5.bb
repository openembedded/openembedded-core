require xorg-lib-common.inc
PE = "1"

DESCRIPTION = "X Toolkit Intrinsics"
PRIORITY = "optional"

DEPENDS += " libsm virtual/libx11 xproto kbproto"
PROVIDES = "xt"

XORG_PN = "libXt"

EXTRA_OECONF="--enable-malloc0returnsnull --disable-install-makestrs --disable-xkb"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C util 'XT_CFLAGS=' 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makestrs
	) || exit 1
	oe_runmake
}
