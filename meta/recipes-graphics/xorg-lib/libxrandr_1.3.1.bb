SUMMARY = "XRandR: X Resize, Rotate and Reflect extension library"

DESCRIPTION = "The X Resize, Rotate and Reflect Extension, called RandR \
for short, brings the ability to resize, rotate and reflect the root \
window of a screen. It is based on the X Resize and Rotate Extension as \
specified in the Proceedings of the 2001 Usenix Technical Conference \
[RANDR]."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c5c132d3690478179d78ec3bf064584"

DEPENDS += "virtual/libx11 randrproto libxrender libxext"

PR = "r1"
PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "7785c3f7cff2735c94657e8f87ed8ad3"
SRC_URI[sha256sum] = "62bba708649c04cbbc2f5de910942a01cc727b27225bc06169af8a89b957c661"
