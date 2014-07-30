include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
"

SRC_URI[md5sum] = "594c0c06eaace9b9d3bad010de1bdfae"
SRC_URI[sha256sum] = "23c39fdc2b24f889b07cab0449825384fef7592a121e180729fd9025ec45c695"
S = "${WORKDIR}/gstreamer-${PV}"

