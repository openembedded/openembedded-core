DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

PR = "r1"

SECTION = "x11/gnome/libs"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy intltool-native"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'x11', 'gnome', '', d)}"
PACKAGECONFIG[gnome] = "--with-gnome,--without-gnome,libgnome-keyring sqlite3"

SHRT_VER = "${@bb.data.getVar('PV',d,1).split('.')[0]}.${@bb.data.getVar('PV',d,1).split('.')[1]}"
SRC_URI = "${GNOME_MIRROR}/libsoup/${SHRT_VER}/libsoup-${PV}.tar.xz"

SRC_URI[md5sum] = "211ec6b733d4de33056b56838c88436e"
SRC_URI[sha256sum] = "32e81220f53abb1f5bbe7d8b0717119df70667fc48e2342d82209ed1593e71dc"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
