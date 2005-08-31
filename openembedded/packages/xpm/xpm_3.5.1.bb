SECTION = "x11/libs"
LICENSE = "X-BSD"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto x11"
DESCRIPTION = "X Pixmap library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXpm-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXpm-${PV}"

inherit autotools pkgconfig 

do_stage () {
	install -m 0644 ${S}/lib/xpm.h ${STAGING_INCDIR}/X11/xpm.h
	oe_libinstall -a -so -C lib libXpm ${STAGING_LIBDIR}
}
