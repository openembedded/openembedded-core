DESCRIPTION = "Library to read the extended image information (EXIF) from JPEG pictures"
HOMEPAGE = "http://sourceforge.net/projects/libexif"
SECTION = "libs"
LICENSE = "LGPL"
DEPENDS = "gettext"

SRC_URI = "${SOURCEFORGE_MIRROR}/libexif/libexif-${PV}.tar.bz2"

SRC_URI[md5sum] = "deee153b1ded5a944ea05d041d959eca"
SRC_URI[sha256sum] = "db6885d5e40e3a273ff8bb9708ab739c8ace3c5abdd75509eec8ea31a31aac43"

inherit autotools

do_configure_append() {
	sed -i s:doc\ binary:binary:g Makefile
}

