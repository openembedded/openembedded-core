require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "${INC_PR}.1"

SRC_URI += "file://vmware-port-vmware-driver-to-new-compat-API.patch"
SRC_URI[md5sum] = "0743ec7c479603fba60d118858fd5783"
SRC_URI[sha256sum] = "c827875fd94805ec9b925fe09aaa973e7e3f9096c7ef311d449c3f134ae75147"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'
