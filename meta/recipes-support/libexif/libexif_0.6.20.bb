DESCRIPTION = "Library to read the extended image information (EXIF) from JPEG pictures"
HOMEPAGE = "http://sourceforge.net/projects/libexif"
SECTION = "libs"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=243b725d71bb5df4a1e5920b344b86ad"

SRC_URI = "${SOURCEFORGE_MIRROR}/libexif/libexif-${PV}.tar.bz2"

SRC_URI[md5sum] = "19844ce6b5d075af16f0d45de1e8a6a3"
SRC_URI[sha256sum] = "a772d20bd8fb9802d7f0d70fde6ac8872f87d0c66c52b0d14026dafcaa83d715"

inherit autotools gettext

do_configure_append() {
	sed -i s:doc\ binary:binary:g Makefile
}

