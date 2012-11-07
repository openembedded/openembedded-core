require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

PR = "${INC_PR}.0"

DEPENDS += "virtual/libx11 drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

PACKAGECONFIG ??= ""
PACKAGECONFIG[sna] = "--enable-sna,--disable-sna"
PACKAGECONFIG[xvmc] = "--enable-xvmc,--disable-xvmc,libxvmc"

# --enable-kms-only option is required by ROOTLESS_X
EXTRA_OECONF += '${@base_conditional( "ROOTLESS_X", "1", " --enable-kms-only", "", d )}'

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "6d9565de03c167d8f621315476c20c73"
SRC_URI[sha256sum] = "39e02b7f90a2665efe5483075f93b1c87d24f48070d5de783dd41e20d9eb0c7c"
