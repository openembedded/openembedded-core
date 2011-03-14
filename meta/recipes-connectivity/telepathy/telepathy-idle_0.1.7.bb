SUMMARY = "Telepathy IRC connection manager"
DESCRIPTION = "Telepathy implementation of the Internet Relay Chat protocols."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib openssl"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/idle.c;beginline=1;endline=19;md5=b06b1e2594423111a1a7910b0eefc7f9"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/${BPN}/${P}.tar.gz"

SRC_URI[md5sum] = "5ee3aa5c6e1112922b11711e6a209331"
SRC_URI[sha256sum] = "b65df4f8ebdf1039e1f1e406f53cc7b6106fab6c4d8e784e360709f8d85df0c3"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
