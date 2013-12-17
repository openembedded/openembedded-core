DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

SECTION = "x11/gnome/libs"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 intltool-native"

# libsoup-gnome is entirely deprecated and just stubs in 2.42 onwards.  Enable
# by default but let it be easily disabled.
PACKAGECONFIG ??= "gnome"
PACKAGECONFIG[gnome] = "--with-gnome,--without-gnome"

SHRT_VER = "${@bb.data.getVar('PV',d,1).split('.')[0]}.${@bb.data.getVar('PV',d,1).split('.')[1]}"
SRC_URI = "${GNOME_MIRROR}/libsoup/${SHRT_VER}/libsoup-${PV}.tar.xz"

SRC_URI[md5sum] = "92aa3667357157e8f3489bcca287f2fa"
SRC_URI[sha256sum] = "e7e4b5ab74a6c00fc267c9f5963852d28759ad3154dab6388e2d6e1962d598f3"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
