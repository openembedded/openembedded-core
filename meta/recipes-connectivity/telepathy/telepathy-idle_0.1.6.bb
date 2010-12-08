SUMMARY = "Telepathy IRC connection manager"
DESCRIPTION = "Telepathy implementation of the Internet Relay Chat protocols."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib openssl"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/idle.c;beginline=1;endline=19;md5=b06b1e2594423111a1a7910b0eefc7f9"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/${PN}/${P}.tar.gz"

SRC_URI[md5sum] = "0efe17425cdc3490714947ff68003bf6"
SRC_URI[sha256sum] = "fc05a1780499eab3b168b1826e2fe18dbe4bea3576ea0ff2b548b063336c61f5"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
