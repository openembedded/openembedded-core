require xorg-util-common.inc

DESCRIPTION = "X autotools macros"
PR = "r2"
PE = "1"

SRC_URI += "file://unbreak_cross_compile.patch;patch=1"

XORG_PN = "util-macros"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${DEBPV})"
