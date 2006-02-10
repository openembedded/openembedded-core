PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "xt xext"
PR = "r2"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xmu"
S = "${WORKDIR}/Xmu"

PACKAGES =+ "xmuu xmuu-dev"

FILES_xmuu = "${libdir}/libXmuu.so.*"
FILES_xmuu-dev = "${libdir}/libXmuu.so"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
