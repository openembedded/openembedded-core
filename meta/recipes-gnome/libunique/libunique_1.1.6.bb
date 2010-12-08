SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libunique/1.1/libunique-${PV}.tar.bz2"

SRC_URI[md5sum] = "7955769ef31f1bc4f83446dbb3625e6d"
SRC_URI[sha256sum] = "e5c8041cef8e33c55732f06a292381cb345db946cf792a4ae18aa5c66cdd4fbb"
PR = "r1"

DEPENDS = "glib-2.0 gtk+ dbus"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"
#S = "${WORKDIR}/unique-${PV}"

inherit autotools
