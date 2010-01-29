require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- Intel i8xx, i9xx display driver"
DEPENDS += "virtual/libx11 libxvmc drm dri2proto glproto \
	    virtual/libgl xineramaproto libpciaccess"

PV = "2.10.0+git${SRCPV}"
PR = "r0"

EXTRA_OECONF += "--disable-xvmc --enable-dri --disable-static"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/driver/xf86-video-intel;protocol=git"

S = "${WORKDIR}/git"

COMPATIBLE_HOST = '(i.86.*-linux)'


