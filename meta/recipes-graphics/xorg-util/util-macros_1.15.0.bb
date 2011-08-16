SUMMARY = "X autotools macros"

DESCRIPTION = "M4 autotools macros used by various X.org programs."

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=1970511fddd439b07a6ba789d28ff662"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "libgpg-error"
DEPENDS_virtclass-native = "virtual/gettext-native"
DEPENDS_virtclass-nativesdk = "virtual/gettext-nativesdk"

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "c7f0c94126443f6744328a92d2b94cff"
SRC_URI[sha256sum] = "db05c59c0e7843c398ed2847ba4b5bf54292499e5fcadbb7c38a445bf4347ab8"
