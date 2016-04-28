include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
"

SRC_URI[md5sum] = "b6f47bcb3d924f7ef8a8b33ac4d037ab"
SRC_URI[sha256sum] = "8e656a9a3be60d7e7ed3fb8e2a22d070b1f54f95d0b22accd876360e659446ce"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
