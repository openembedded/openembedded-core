require gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
    file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
"
SRC_URI[md5sum] = "198b7a8eff3e0206d3b5660389df83ef"
SRC_URI[sha256sum] = "c91597d03abff9df435ad4892eae44df1ee14159c7cc7317ac9d2766ff446bd2"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
