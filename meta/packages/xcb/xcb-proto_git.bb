DEFAULT_PREFERENCE = "-1"

include xcb-proto.inc
PV = "1.0+git"
PR = "r0"

SRC_URI = "git://anongit.freedesktop.org/git/xcb;protocol=git"
S = "${WORKDIR}/git/xcb-proto"

do_stage() {
	autotools_stage_all
}

