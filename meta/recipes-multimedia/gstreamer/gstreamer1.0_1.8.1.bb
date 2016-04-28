include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
"

SRC_URI[md5sum] = "711ada79b63e47ac96adb4e5444dc908"
SRC_URI[sha256sum] = "5a3722fb9302dd977c17ced4240293dc777cb716dc98c8cca63d75c27e5e3107"

S = "${WORKDIR}/gstreamer-${PV}"
