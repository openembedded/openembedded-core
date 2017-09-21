require gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
    file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
"
SRC_URI[md5sum] = "8a0ba8141b1548ee094eb97e7cf5471f"
SRC_URI[sha256sum] = "e88ca584c94ea78eeecbf3af00ef7f134b66bdee7408aa4aa6c547235e060052"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
