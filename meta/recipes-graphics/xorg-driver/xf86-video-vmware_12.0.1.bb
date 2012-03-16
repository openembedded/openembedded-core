require xorg-driver-video.inc

SUMMARY = "X.Org X server -- VMware SVGA display driver"

DESCRIPTION = "vmware is an Xorg driver for VMware virtual video cards."

LIC_FILES_CHKSUM = "file://COPYING;md5=5fcd7d437a959a15fbee8707747c6b53"

DEPENDS += "virtual/libx11 libxvmc drm glproto \
	    virtual/libgl xineramaproto libpciaccess"

PR = "${INC_PR}.1"

SRC_URI[md5sum] = "08101a9b09774ec2f432db5118928c53"
SRC_URI[sha256sum] = "ded644af1f74ca0ebdc759e3db715519c476b341c1783c3d92c93a76591ed496"

COMPATIBLE_HOST = '(i.86.*-linux|x86_64.*-linux)'

do_install_append () {
	# Remove useless empty directory
	rmdir ${D}${bindir}
}
