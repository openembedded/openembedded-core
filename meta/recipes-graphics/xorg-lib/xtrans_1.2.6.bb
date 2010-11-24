DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=49347921d4d5268021a999f250edc9ca"

PE = "1"
PR = "r0"

RDEPENDS_${PN}-dev = ""
DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"
