include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "4293ca4d8333690d5acdffe3ad354924"
SRC_URI[sha256sum] = "b9f12137ab663edc6c37429b38ca7911074b9c2a829267fe855d4e57d916a0b6"
S = "${WORKDIR}/gstreamer-${PV}"

