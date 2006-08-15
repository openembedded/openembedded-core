PV = "6.0.3+cvs${SRCDATE}"
LICENSE = "MIT-X"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11 libice"
DESCRIPTION = "Session management library"
PR = "r1"

DEFAULT_PREFERENCE="-1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=SM"
S = "${WORKDIR}/SM"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
