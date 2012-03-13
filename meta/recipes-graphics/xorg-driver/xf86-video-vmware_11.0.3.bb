require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "${INC_PR}.1"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

SRC_URI[md5sum] = "579bee487309b1bdc8329bf627d43413"
SRC_URI[sha256sum] = "b24a7cb2d87e416561e25122eab2cd48fc64a2ba105238456eefef16f29f38a3"

do_install_append () {
	# Remove useless empty directory
	rmdir ${D}${bindir}
}