include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
"

SRC_URI[md5sum] = "0f011ee793cbcfa96d6b51d8271349fa"
SRC_URI[sha256sum] = "9dbebe079c2ab2004ef7f2649fa317cabea1feb4fb5605c24d40744b90918341"

S = "${WORKDIR}/gstreamer-${PV}"
