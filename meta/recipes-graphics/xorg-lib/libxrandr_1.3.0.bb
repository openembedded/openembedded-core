DESCRIPTION = "X11 Resize and Rotate extension library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=660ef232af253760087c069d76287b85"

DEPENDS += "randrproto libxrender libxext"

PR = "r0"
PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "nativesdk"
