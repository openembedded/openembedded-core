require xf86-video-common.inc

EXTRA_OECONF += "--disable-xvmc"

DESCRIPTION = "X.Org X server -- Generic Vesa display driver"
DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'
