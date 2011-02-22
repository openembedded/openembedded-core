require xf86-video-common.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

PR = "r1"

EXTRA_OECONF += "--disable-xvmc"

# --enable-kms-only option is required by ROOTLESS_X
EXTRA_OECONF += '${@base_conditional( "ROOTLESS_X", "1", " --enable-kms-only", "", d )}'

DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "05f187582aeabda57fcd6f2782cfbf8e"
SRC_URI[sha256sum] = "e18c37a579a960516e69de5c6f74750ca02208c0e41cf763ae5630c84db507df"
