SUMMARY = "Xlib: C Language X Interface library"

DESCRIPTION = "This package provides a client interface to the X Window \
System, otherwise known as 'Xlib'.  It provides a complete API for the \
basic functions of the window system."

require xorg-lib-common.inc

PROVIDES = "virtual/libx11"

XORG_PN = "libX11"
LEAD_SONAME = "libX11.so"

EXTRA_OECONF += "--with-keysymdef=${STAGING_INCDIR}/X11/keysymdef.h"

FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB ${libdir}/X11/Xcms.txt"
FILES_${PN}-locale += "${datadir}/X11/locale ${libdir}/X11/locale"

do_compile() {
	cd ${S}/src/util
	mv makekeys.c.orig makekeys.c || true
	touch makekeys-makekeys.o
	(
		unset CC LD CXX CCLD CFLAGS CPPFLAGS LDFLAGS CXXFLAGS
		${BUILD_CC} ${BUILD_CFLAGS} -I${STAGING_INCDIR_NATIVE} makekeys.c -o makekeys
	)
	if [ "$?" != "0" ]; then
		exit 1
	fi
	# mv to stop it getting rebuilt
	mv makekeys.c makekeys.c.orig
	cd ${S}
	oe_runmake
}

# Multiple libx11 derivatives from from this file and are selected by virtual/libx11
# A world build should only build the correct version, not all of them.
EXCLUDE_FROM_WORLD = "1"
