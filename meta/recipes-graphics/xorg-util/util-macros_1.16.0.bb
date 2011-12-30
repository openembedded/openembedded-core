SUMMARY = "X autotools macros"

DESCRIPTION = "M4 autotools macros used by various X.org programs."

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=1970511fddd439b07a6ba789d28ff662"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "3f77f0b6452c677d4b5ce9feaea7d15e"
SRC_URI[sha256sum] = "1387cec5c2e655811e9a60afee7bda1652f8cd97be6c89418566bcf934ff6935"
