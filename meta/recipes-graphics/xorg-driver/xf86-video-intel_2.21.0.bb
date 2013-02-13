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

SRC_URI[md5sum] = "f029cc261fca75b32ba85b9c4189db88"
SRC_URI[sha256sum] = "d872adef06cbf1a4434811baad4b8a18feacc6633b59b36557e8d7db7161081c"
