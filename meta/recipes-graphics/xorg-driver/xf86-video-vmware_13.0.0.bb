require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

SRC_URI += "file://Kill-mibstore.patch"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "b72be57517cbc7a56362f2ee0f092269"
SRC_URI[sha256sum] = "44919ecd88654f2ab40a1732b5ea6a1dbd1c696b4b0748e68e8b5f1a3486bca6"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'
