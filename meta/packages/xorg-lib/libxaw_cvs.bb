PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "xproto virtual/libx11 libxt libxmu libxpm"
PROVIDES = "xaw"
DESCRIPTION = "X Athena Widgets library"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xaw"
S = "${WORKDIR}/Xaw"

inherit autotools pkgconfig 

# FIXME: libXaw needs a full x11, not diet
BROKEN = "1"

do_stage () {
	oe_runmake install DESTDIR="" mandir=${STAGING_DATADIR}/man bindir=${STAGING_BINDIR} includedir=${STAGING_INCDIR} libdir=${STAGING_LIBDIR} prefix=${STAGING_DIR}
}
