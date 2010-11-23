DESCRIPTION = "Telepathy framework - Python package"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/utils.py;beginline=1;endline=17;md5=9a07d1a9791a7429a14e7b25c6c86822"

RDEPENDS_${PN} += "python-dbus"

SRC_URI = "http://telepathy.freedesktop.org/releases/${PN}/${P}.tar.gz "

PR = "r0"

inherit autotools

SRC_URI[md5sum] = "51da78a77681b0652d9b4ca941da0658"
SRC_URI[sha256sum] = "f9f5c260188e9e27af9152bfc6d622cc5c0ea48d63d5fa9985abbdd69fda0e87"
