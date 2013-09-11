require xorg-driver-video.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a1f0610ebdc6f314a9fa5102a8c5c1b0"

SUMMARY = "X.Org X server -- Generic Vesa video driver"

DESCRIPTION = "vesa is an Xorg driver for generic VESA video cards. It \
can drive most VESA-compatible video cards, but only makes use of the \
basic standard VESA core that is common to these cards. The driver \
supports depths 8, 15 16 and 24."

PR = "${INC_PR}.0"

DEPENDS += "virtual/libx11 randrproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

RRECOMMENDS_${PN} += "xserver-xorg-module-libint10"

SRC_URI += "file://Remove-mibstore.h.patch"

SRC_URI[md5sum] = "3eddd393fba79550e012d717499d58ad"
SRC_URI[sha256sum] = "144a17ffae3c86603ddc4ae33521a52813498ee1f8213faa662dc4a8d6490ee3"
