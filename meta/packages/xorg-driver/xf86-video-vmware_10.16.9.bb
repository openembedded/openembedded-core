require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- VMware SVGA II display driver"
DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

COMPATIBLE_HOST = '(i.86.*-linux)'