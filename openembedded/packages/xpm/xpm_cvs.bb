PV = "0.0+cvs${SRCDATE}"
LICENSE = "X-BSD"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto x11"
DESCRIPTION = "X Pixmap library."
PR = "r1"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xpm"
S = "${WORKDIR}/Xpm"

inherit autotools pkgconfig 

do_stage () {
	install -m 0644 ${S}/lib/xpm.h ${STAGING_INCDIR}/X11/xpm.h
	oe_libinstall -a -so -C lib libXpm ${STAGING_LIBDIR}
}
