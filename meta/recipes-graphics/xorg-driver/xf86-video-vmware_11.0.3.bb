require xf86-video-common.inc

DESCRIPTION = "X.Org X server -- VMware SVGA II display driver"

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "r0"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'
