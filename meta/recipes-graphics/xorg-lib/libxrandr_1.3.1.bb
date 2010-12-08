DESCRIPTION = "X11 Resize and Rotate extension library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c5c132d3690478179d78ec3bf064584"

DEPENDS += "randrproto libxrender libxext"

PR = "r0"
PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "7785c3f7cff2735c94657e8f87ed8ad3"
SRC_URI[sha256sum] = "62bba708649c04cbbc2f5de910942a01cc727b27225bc06169af8a89b957c661"
