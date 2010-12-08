DESCRIPTION = "Userspace interface to kernel DRM services"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://MIT_License.txt;md5=732825ecdcf420261531d935fcd914a7"
PR = "r4"

PROVIDES = "libdrm-poulsbo"

inherit autotools pkgconfig

SRC_URI = "https://launchpad.net/~gma500/+archive/ppa/+files/libdrm-poulsbo_2.3.0-1ubuntu0sarvatt4~1004um1ubuntu1.tar.gz \
	file://libdrm-poulsbo.patch"

SRC_URI[md5sum] = "82c00bb9f1239f1d00b18411b89c2380"
SRC_URI[sha256sum] = "335d4ac6694687475efe07b44a2d77a9a9fadcbc16946d01ea8a02339ae7d9ec"

do_configure_prepend() {
	sed -e 's/-ldrm/-ldrm_poulsbo/' -e 's/libdrm/libdrm_poulsbo/' \
	    < ${S}/libdrm.pc.in > ${S}/libdrm_poulsbo.pc.in
}
