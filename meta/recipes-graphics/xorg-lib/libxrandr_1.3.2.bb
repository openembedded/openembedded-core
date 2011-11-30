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

PR = "r0"
PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "92473da2fccf5fac665be4fa4f2037fa"
SRC_URI[sha256sum] = "7eaca216ab5233d7396119eb87c1989d350a3efead104d54b55f22cdd1d99b81"
