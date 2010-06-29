DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=e334229ea6378ccb4a6202d7b715831c"

PE = "1"
PR = "r0"

RDEPENDS_${PN}-dev = ""
DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"
