require xorg-util-common.inc

DESCRIPTION = "X autotools macros"
PE = "1"

SRC_URI += "file://unbreak_cross_compile.patch;patch=1"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "gettext"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"

BBCLASSEXTEND = "native nativesdk"
