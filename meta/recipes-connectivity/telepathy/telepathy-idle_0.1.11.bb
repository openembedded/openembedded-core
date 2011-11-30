SUMMARY = "Telepathy IRC connection manager"
DESCRIPTION = "Telepathy implementation of the Internet Relay Chat protocols."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib openssl"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/idle.c;beginline=1;endline=19;md5=b06b1e2594423111a1a7910b0eefc7f9"
PR = "r1"

SRC_URI = "http://telepathy.freedesktop.org/releases/${BPN}/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "be277e8f0bd32ae56af799f0ff4ec72e"
SRC_URI[sha256sum] = "047ccbec0b91ac187467ede5a90966f9ec881df56fd8c80cb3bce3710f66fda0"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
