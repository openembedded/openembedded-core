DESCRIPTION = "X autotools macros"

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=1970511fddd439b07a6ba789d28ff662"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "gettext"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"

BBCLASSEXTEND = "native nativesdk"
