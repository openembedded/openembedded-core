require xorg-lib-common.inc

DESCRIPTION = "Base X libs."

DEPENDS += " bigreqsproto xproto xextproto xtrans libxau xcmiscproto \
	libxdmcp xf86bigfontproto kbproto inputproto"
PROVIDES = "virtual/libx11"
RPROVIDES = "virtual/libx11"

XORG_PN = "libX11"

FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB ${libdir}/X11/Xcms.txt"
FILES_${PN}-locale += "${datadir}/X11/locale ${libdir}/X11/locale"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C src/util 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makekeys
	)
	rm -f ${STAGING_INCDIR}/X11/Xlib.h
	oe_runmake
}
