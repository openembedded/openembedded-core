DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

PR = "r2"

SECTION = "x11/gnome/libs"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SHRT_VER = "${@bb.data.getVar('PV',d,1).split('.')[0]}.${@bb.data.getVar('PV',d,1).split('.')[1]}"
SRC_URI = "${GNOME_MIRROR}/libsoup/${SHRT_VER}/libsoup-${PV}.tar.xz"

SRC_URI[md5sum] = "d13fb4968acea24c26b83268a308f580"
SRC_URI[sha256sum] = "71b8923fc7a5fef9abc5420f7f3d666fdb589f43a8c50892d584d58b3c513f9a"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

# glib-networking is needed for SSL, proxies, etc.
RRECOMMENDS_${PN} = "glib-networking"
