include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
"

SRC_URI[md5sum] = "d9daccca9dffdbcef4373cde81f9ae2c"
SRC_URI[sha256sum] = "53657ffb7d49ddc4ae40e3f52e56165db4c06eb016891debe2b6c0e9f134eb8c"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
