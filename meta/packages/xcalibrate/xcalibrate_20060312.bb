PV = "0.0+cvs${FIXEDSRCDATE}"
FIXEDSRCDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
LICENSE = "BSD-X"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11 xcalibrateext libxext"
DESCRIPTION = "XCalibrate client-side library"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=XCalibrate \
	   file://xextproto.patch;patch=1"
S = "${WORKDIR}/XCalibrate"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
