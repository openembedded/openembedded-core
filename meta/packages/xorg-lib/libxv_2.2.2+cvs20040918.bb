LICENSE = "GPL"
SECTION = "x11/libs"
DEPENDS = "virtual/libx11 libxext"
DESCRIPTION = "X Video extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xv;date=20040918;method=pserver"
S = "${WORKDIR}/Xv"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
