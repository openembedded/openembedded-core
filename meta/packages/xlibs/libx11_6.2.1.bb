SECTION = "x11/libs"
LICENSE = "XFree86"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto xextensions libxau xtrans libxdmcp"
PROVIDES = "x11"
DESCRIPTION = "Base X libs."
FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB"
FILES_${PN}-locale += "${datadir}/X11/locale"
PR = "r4"

SRC_URI = "${XLIBS_MIRROR}/libX11-${PV}.tar.bz2 \
           file://errordb-keysymdb-path-fix.patch;patch=1 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libX11-${PV}"

inherit autotools pkgconfig 

do_compile() {
	(
		unset CC LD CXX CCLD
#		unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
		oe_runmake -C src/util 'CFLAGS= -D_GNU_SOURCE' 'LDFLAGS=' 'CXXFLAGS=' 'CPPFLAGS=' makekeys
	)
	oe_runmake
}

do_stage() {
	install -c -m 644 include/X11/XKBlib.h ${STAGING_INCDIR}/X11/XKBlib.h
	install -c -m 644 include/X11/Xcms.h ${STAGING_INCDIR}/X11/Xcms.h
	install -c -m 644 include/X11/Xlib.h ${STAGING_INCDIR}/X11/Xlib.h
	install -c -m 644 include/X11/Xlibint.h ${STAGING_INCDIR}/X11/Xlibint.h
	install -c -m 644 include/X11/Xlocale.h ${STAGING_INCDIR}/X11/Xlocale.h
	install -c -m 644 include/X11/Xresource.h ${STAGING_INCDIR}/X11/Xresource.h
	install -c -m 644 include/X11/Xutil.h ${STAGING_INCDIR}/X11/Xutil.h
	install -c -m 644 include/X11/cursorfont.h ${STAGING_INCDIR}/X11/cursorfont.h
	install -c -m 644 include/X11/region.h ${STAGING_INCDIR}/X11/region.h

	oe_libinstall -a -so -C src libX11 ${STAGING_LIBDIR}
}
