include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
    file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
"
SRC_URI[md5sum] = "646ab511bc8e56425e63d3fc4812e582"
SRC_URI[sha256sum] = "a5ecd59fc2091eeb52368de81cc6a91c1a1c19dc5bdde85ce90e1eed5d4183c2"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
