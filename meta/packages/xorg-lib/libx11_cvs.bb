PV = "6.2.1+cvs${SRCDATE}"
LICENSE = "XFree86"
PR = "r6"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto xextensions libxau xtrans libxdmcp"
PROVIDES = "virtual/libx11"
RPROVIDES = "virtual/libx11"
DESCRIPTION = "Base X libs."
FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB"
FILES_${PN}-locale += "${datadir}/X11/locale"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=X11 \
	   file://errordb-keysymdb-path-fix.patch;patch=1"
S = "${WORKDIR}/X11"

inherit autotools pkgconfig 

do_compile() {
	(
		unset CC LD CXX CCLD
#		unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
		oe_runmake -C src/util 'CFLAGS=' 'LDFLAGS=' 'CXXFLAGS=' 'CPPFLAGS=' makekeys
	)
	rm -f ${STAGING_INCDIR}/X11/Xlib.h
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
