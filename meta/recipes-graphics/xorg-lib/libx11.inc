SUMMARY = "Xlib: C Language X Interface library"

DESCRIPTION = "This package provides a client interface to the X Window \
System, otherwise known as 'Xlib'.  It provides a complete API for the \
basic functions of the window system."

require xorg-lib-common.inc

inherit siteinfo

PE = "1"

PROVIDES = "virtual/libx11"

XORG_PN = "libX11"
LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=172255dee66bb0151435b2d5d709fcf7"

EXTRA_OECONF += "--with-groff=no --with-ps2pdf=no --with-fop=no --disable-specs"

PACKAGES =+ "${PN}-xcb"

FILES_${PN} += "${datadir}/X11/XKeysymDB ${datadir}/X11/XErrorDB ${libdir}/X11/Xcms.txt"
FILES_${PN}-xcb += "${libdir}/libX11-xcb.so.*"
FILES_${PN}-locale += "${datadir}/X11/locale ${libdir}/X11/locale"

do_compile_prepend() {
	cd ${S}/src/util
	mv makekeys.c.orig makekeys.c || true
	touch makekeys-makekeys.o
	(
		unset CC LD CXX CCLD CFLAGS CPPFLAGS LDFLAGS CXXFLAGS
		# MIN_REHASH 10 is only in 1.0.1
		sed -i -e 's:MIN_REHASH 10:MIN_REHASH 16:g' makekeys.c
		sed -i -e 's:MIN_REHASH 15:MIN_REHASH 16:g' makekeys.c
		touch makekeys-makekeys.o;
		if [ "${SITEINFO_BITS}" == "64" ]; then
			${BUILD_CC} ${BUILD_CFLAGS} -I${STAGING_INCDIR_NATIVE} makekeys.c -o makekeys
		else
			${BUILD_CC} ${BUILD_CFLAGS} -I${STAGING_INCDIR_NATIVE} -DUSE32 makekeys.c -o makekeys
		fi
	)
	if [ "$?" != "0" ]; then
		exit 1
	fi
	# mv to stop it getting rebuilt
	mv makekeys.c makekeys.c.orig
	cd ../../
}

# Multiple libx11 derivatives from from this file and are selected by virtual/libx11
# A world build should only build the correct version, not all of them.
EXCLUDE_FROM_WORLD = "1"
