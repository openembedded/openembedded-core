PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
DEPENDS = "x11 damageext libxfixes xproto"
DESCRIPTION = "X Damage extension library."

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xdamage \
	   file://m4.patch;patch=1"
S = "${WORKDIR}/Xdamage"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
