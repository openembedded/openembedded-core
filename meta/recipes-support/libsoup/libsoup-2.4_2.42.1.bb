DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

SECTION = "x11/gnome/libs"

DEPENDS = "glib-2.0 gnutls libxml2 sqlite3 intltool-native"

# libsoup-gnome is entirely deprecated and just stubs in 2.42 onwards.  Enable
# by default but let it be easily disabled.
PACKAGECONFIG ??= "gnome"
PACKAGECONFIG[gnome] = "--with-gnome,--without-gnome"

SHRT_VER = "${@bb.data.getVar('PV',d,1).split('.')[0]}.${@bb.data.getVar('PV',d,1).split('.')[1]}"
SRC_URI = "${GNOME_MIRROR}/libsoup/${SHRT_VER}/libsoup-${PV}.tar.xz"

SRC_URI[md5sum] = "aaccd590a8002b69716f885fa8589887"
SRC_URI[sha256sum] = "20bd5365bb3e7de70fade91ac82c9dea31636318df90b8c26bc4cde2523a8c91"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
