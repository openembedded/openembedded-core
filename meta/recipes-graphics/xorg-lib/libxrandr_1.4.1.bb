SUMMARY = "XRandR: X Resize, Rotate and Reflect extension library"

DESCRIPTION = "The X Resize, Rotate and Reflect Extension, called RandR \
for short, brings the ability to resize, rotate and reflect the root \
window of a screen. It is based on the X Resize and Rotate Extension as \
specified in the Proceedings of the 2001 Usenix Technical Conference \
[RANDR]."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=aa33e583cc3e96238a8467b485e62992"

DEPENDS += "virtual/libx11 randrproto libxrender libxext"

PE = "1"

XORG_PN = "libXrandr"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "8dbac4e972348dcbd3a0673ce2e37919"
SRC_URI[sha256sum] = "d914a0490fd0a2ea6c3194505b5b28c56e2a277d8f4648b0275ee0ee370fb905"
