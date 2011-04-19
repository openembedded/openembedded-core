SUMMARY = "X autotools macros"

DESCRIPTION = "M4 autotools macros used by various X.org programs."

require xorg-util-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=1970511fddd439b07a6ba789d28ff662"

PE = "1"
PR = "r0"

# ${PN} is empty so we need to tweak -dev and -dbg package dependencies
DEPENDS = "libgpg-error"
DEPENDS_virtclass-native = "gettext"
DEPENDS_virtclass-nativesdk = "gettext"

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "31e9ddcbc1d8bc8c09ab180443974dd1"
SRC_URI[sha256sum] = "7bff944fb120192e7fe1706e9c0b7e41666e7983ce3e2bdef0b7734392d9e695"
