require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

PR = "${INC_PR}.0"

DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

SRC_URI[md5sum] = "6d7b1f199dba5820f250888b136186ff"
SRC_URI[sha256sum] = "8b8450f2a2cc52ef31a83414e2f290e748a956690e11b41759d5650aaedc4387"

EXTRA_OECONF += "--disable-xvmc"

# --enable-kms-only option is required by ROOTLESS_X
EXTRA_OECONF += '${@base_conditional( "ROOTLESS_X", "1", " --enable-kms-only", "", d )}'

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'
