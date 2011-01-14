require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- Intel i8xx, i9xx display driver"

LIC_FILES_CHKSUM = "file://COPYING;md5=8730ad58d11c7bbad9a7066d69f7808e"

EXTRA_OECONF += "--disable-xvmc"

DEPENDS += "virtual/libx11 libxvmc drm xf86driproto glproto \
	    virtual/libgl xineramaproto xf86driproto libpciaccess"

COMPATIBLE_HOST = '(i.86|x86_64).*-linux'

SRC_URI[md5sum] = "05f187582aeabda57fcd6f2782cfbf8e"
SRC_URI[sha256sum] = "e18c37a579a960516e69de5c6f74750ca02208c0e41cf763ae5630c84db507df"
