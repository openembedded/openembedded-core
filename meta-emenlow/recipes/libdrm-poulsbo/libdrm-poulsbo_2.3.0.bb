DESCRIPTION = "Userspace interface to kernel DRM services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://MIT_License.txt;md5=732825ecdcf420261531d935fcd914a7"
PR = "r4"

PROVIDES = "libdrm-poulsbo"

inherit autotools pkgconfig

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/libdrm-poulsbo_2.3.0-1ubuntu0sarvatt4~1004um1ubuntu1.tar.gz \
	file://libdrm-poulsbo.patch"

do_configure_prepend() {
	sed -e 's/-ldrm/-ldrm_poulsbo/' -e 's/libdrm/libdrm_poulsbo/' \
	    < ${S}/libdrm.pc.in > ${S}/libdrm_poulsbo.pc.in
}
