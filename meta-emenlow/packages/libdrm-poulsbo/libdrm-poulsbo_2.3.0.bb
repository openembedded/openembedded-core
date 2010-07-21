DESCRIPTION = "Userspace interface to kernel DRM services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://MIT_License.txt;md5=732825ecdcf420261531d935fcd914a7"
PR="r1"

PROVIDES = "libdrm-poulsbo libdrm drm"

inherit autotools pkgconfig

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/libdrm-poulsbo_2.3.0-1ubuntu0sarvatt4~1004um1ubuntu1.tar.gz"

do_install_append() {
	cp ${D}/${libdir}/pkgconfig/libdrm.pc ${D}/${libdir}/pkgconfig/libdrm-poulsbo.pc 
}
