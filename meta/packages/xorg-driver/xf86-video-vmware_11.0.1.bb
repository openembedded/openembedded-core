require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- VMware SVGA II display driver"

LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "r1"

COMPATIBLE_HOST = '(i.86.*-linux)'
