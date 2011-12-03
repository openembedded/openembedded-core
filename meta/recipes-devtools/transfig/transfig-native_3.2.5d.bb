SUMMARY = "Utilities for converting XFig figure files"
DESCRIPTION = "This package contains utilities (mainly fig2dev) to \
handle XFig (Facility for Interactive Generation of figures) files."
HOMEPAGE = "http://www-epb.lbl.gov/xfig/"
SECTION = "console/utils"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://fig2dev/fig2dev.c;endline=16;md5=6bab01e017409cf9ab21d3e953b793f7"
PR = "r1"

DEPENDS = "imake-native xorg-cf-files-native zlib-native jpeg-native libpng-native libxpm-native"

SRC_URI = "${SOURCEFORGE_MIRROR}/mcj/transfig.${PV}.tar.gz"

SRC_URI[md5sum] = "f9eac7f265668ecbfda6aaf7581989ad"
SRC_URI[sha256sum] = "ae81214177fb05f91f6e43b0b42633b6e0024570cbc6591a3858e12100ce8aaa"

S = "${WORKDIR}/transfig.${PV}"

EXTRA_OEMAKE = "-I${S}"

inherit native

do_configure() {
	xmkmf
	make Makefiles

	# Fix hardcoded references to host build locations
	sed -i -e "s|SYSTEMUSRLIBDIR = /usr/lib64|SYSTEMUSRLIBDIR = ${libdir}|g" fig2dev/Makefile
	sed -i -e "s|SYSTEMUSRINCDIR = /usr/include|SYSTEMUSRINCDIR = ${includedir}|g" fig2dev/Makefile
	sed -i -e "s|XPMINC = -I/usr/include/X11|XPMINC = -I${includedir}/X11|g" fig2dev/Makefile 
	sed -i -e "s|/usr/lib64|/usr/lib|g" fig2dev/Makefile || true
	sed -i -e "s|/usr/lib64|/usr/lib|g" fig2dev/dev/Makefile || true
	sed -i -e "s|/usr/local/lib|${libdir}|g" fig2dev/Makefile
	sed -i -e "s|/usr/local/lib|${libdir}|g" fig2dev/dev/Makefile
}

do_install() {
	oe_runmake install DESTDIR=${D}
}
