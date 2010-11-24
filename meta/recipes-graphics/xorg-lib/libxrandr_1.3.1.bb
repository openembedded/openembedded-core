DESCRIPTION = "X11 Resize and Rotate extension library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c5c132d3690478179d78ec3bf064584"

DEPENDS += "randrproto libxrender libxext"

PR = "r0"
PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "nativesdk"
