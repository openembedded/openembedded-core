SECTION = "x11/libs"
LICENSE = "BSD-X"
DEPENDS = "randrext x11 libxrender xext"
DESCRIPTION = "X Resize and Rotate extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXrandr-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXrandr-${PV}"

inherit autotools pkgconfig 

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR}
}
