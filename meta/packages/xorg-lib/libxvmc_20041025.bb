DESCRIPTION = "X Video Motion Compensation extension library."
SECTION = "x11/libs"
DEPENDS = "virtual/libx11 libxext libxv drm xserver-xorg"
PR = "r2"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=XvMC;date=${PV};method=pserver \
	file://true.patch;patch=1 file://drm.patch;patch=1"
S = "${WORKDIR}/XvMC"

CFLAGS += "-I${STAGING_INCDIR}/X11/extensions -I${STAGING_INCDIR}/xserver-xorg -D_BSD_SOURCE"

# this one is for via only atm.
COMPATIBLE_HOST = 'i.86.*-linux'

inherit autotools pkgconfig 

do_compile() {
	oe_runmake
	oe_runmake -C hw/via
}

do_install() {
	oe_runmake DESTDIR='${D}' install
	oe_runmake -C hw/via DESTDIR='${D}' install
}

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
	       mandir=${STAGING_DATADIR}/man
	oe_libinstall -so -C hw/via libviaXvMC ${STAGING_LIBDIR}
	install hw/via/vldXvMC.h ${STAGING_INCDIR}/X11/extensions/
}
