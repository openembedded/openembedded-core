DESCRIPTION = "X autotools macros"

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=b8b2965be9997891cf7ddccdfb242ca8"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "gettext"
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"

BBCLASSEXTEND = "native nativesdk"
