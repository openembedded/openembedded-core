require xorg-proto-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=09d83047c15994e05db29b423ed6662e"

PR = "r0"
PE = "1"

DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"
