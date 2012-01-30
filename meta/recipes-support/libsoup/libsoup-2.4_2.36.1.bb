DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

SECTION = "x11/gnome/libs"
PR = "r1"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SRC_URI = "${GNOME_MIRROR}/libsoup/2.36/libsoup-${PV}.tar.bz2"

SRC_URI[md5sum] = "9c03fc033da61deaf5b403e9e9c884b8"
SRC_URI[sha256sum] = "c387eefd8214c458965b1d6e3490cea33b5bb6c81798cac90cde96136dc19401"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

