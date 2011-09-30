require xorg-driver-video.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=e5418e7a75e21268637984e70265e146"

EXTRA_OECONF += "--disable-xvmc"

SUMMARY = "X.Org X server -- Generic Vesa video driver"

DESCRIPTION = "vesa is an Xorg driver for generic VESA video cards. It \
can drive most VESA-compatible video cards, but only makes use of the \
basic standard VESA core that is common to these cards. The driver \
supports depths 8, 15 16 and 24."

PR = "${INC_PR}.0"

DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "07fa32958aff9b463dd3af5481ef6626"
SRC_URI[sha256sum] = "8ed85a0e94523539d81d5ae6639fa22ceb1c1e3baf89128915db65d4d2900d7a"
