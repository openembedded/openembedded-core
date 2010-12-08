require xorg-proto-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=09d83047c15994e05db29b423ed6662e"

PR = "r0"
PE = "1"

DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "cd7372cd827bfd7ca7e9238f2ce274b1"
SRC_URI[sha256sum] = "730e27e22bfb99409bc364233f3f766f5163de0cbf2edad33738cfdf55f04c15"
