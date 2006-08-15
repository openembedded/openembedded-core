PV = "0.0+cvs${SRCDATE}"
LICENSE = "XFree86"

SECTION = "x11/libs"
DEPENDS = "virtual/libx11 libxext xxf86dgaext"
DESCRIPTION = "Xxf86dga extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xxf86dga"
S = "${WORKDIR}/Xxf86dga"

inherit autotools pkgconfig 

do_stage() {
	oe_libinstall -so -a libXxf86dga ${STAGING_LIBDIR}
}
