SUMMARY = "An HTTP library implementation in C"
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


SRC_URI[md5sum] = "86765c0093efaf3006fa2960d170d097"
SRC_URI[sha256sum] = "fa3d5574c1a2df521242e2ca624a2b3057121798cab9f8f40525aa186a7b15a3"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools gettext pkgconfig

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
