require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- Intel i8xx, i9xx display driver"

EXTRA_OECONF += "--disable-xvmc"

DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "de2f8a5836d90c71f3175dcd46d03ec0"
SRC_URI[sha256sum] = "d8b2fae8d0c4ae372994cb7df8de8aa995b8e89b1bc5766c53ea0751752fc887"
