SUMMARY = "X autotools macros"

DESCRIPTION = "M4 autotools macros used by various X.org programs."

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=1970511fddd439b07a6ba789d28ff662"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "libgpg-error"
DEPENDS_virtclass-native = "virtual/gettext"
DEPENDS_virtclass-nativesdk = "virtual/gettext"

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPKGV})"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "cbe57cd7cc492a762466f5280a7ffba8"
SRC_URI[sha256sum] = "9bd6949b930aadfce32bc9e101008d4c6ff48329ed0e3a467990bee69f574d66"
