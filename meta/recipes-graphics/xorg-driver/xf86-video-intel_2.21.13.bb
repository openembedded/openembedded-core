require xorg-driver-video.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

DEPENDS += "virtual/libx11 drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess udev"

PACKAGECONFIG ??= ""
PACKAGECONFIG[sna] = "--enable-sna,--disable-sna"
PACKAGECONFIG[xvmc] = "--enable-xvmc,--disable-xvmc,libxvmc"

# --enable-kms-only option is required by ROOTLESS_X
EXTRA_OECONF += '${@base_conditional( "ROOTLESS_X", "1", " --enable-kms-only", "", d )}'

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "018154293ea25b69568ff0340a89ae7c"
SRC_URI[sha256sum] = "76e17cebf79d3c3c98af246fcfb55018aa12e2d56fe5f3b6d54b93e90837efa8"
