require xf86-video-common.inc

SUMMARY = "X.Org X server -- Intel integrated graphics chipsets driver"

DESCRIPTION = "intel is an Xorg driver for Intel integrated graphics \
chipsets. The driver supports depths 8, 15, 16 and 24. On some chipsets, \
the driver supports hardware accelerated 3D via the Direct Rendering \
Infrastructure (DRI)."

DEPENDS += "virtual/libx11 libxvmc drm dri2proto glproto \
	    virtual/libgl xineramaproto libpciaccess"

PV = "2.10.0+git${SRCPV}"
PR = "r0"

EXTRA_OECONF += "--disable-xvmc --enable-dri --disable-static"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/driver/xf86-video-intel;protocol=git"

S = "${WORKDIR}/git"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'
