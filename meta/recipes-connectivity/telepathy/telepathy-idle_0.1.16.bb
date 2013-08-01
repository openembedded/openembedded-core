SUMMARY = "Telepathy IRC connection manager"
DESCRIPTION = "Telepathy implementation of the Internet Relay Chat protocols."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib openssl libxslt-native"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/idle.c;beginline=1;endline=19;md5=b06b1e2594423111a1a7910b0eefc7f9"

SRC_URI = "http://telepathy.freedesktop.org/releases/${BPN}/${BPN}-${PV}.tar.gz \
           file://fix-svc-gtk-doc.h-target.patch"
SRC_URI[md5sum] = "afabda908a798aac3a427f4564af113a"
SRC_URI[sha256sum] = "739614e2d526d82bdd2fd94026ca70a46670563556ff15537a2a08bf17172875"

inherit autotools pkgconfig pythonnative

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
