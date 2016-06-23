include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz \
"

SRC_URI[md5sum] = "f781790cf64b44522b01ab560f16d4de"
SRC_URI[sha256sum] = "9c5b33a2a98fc1d6d6c99a1b536b1fb2de45f53cc8bf8ab85a8b8141fed1a8ac"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"
