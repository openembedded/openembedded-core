include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
"
SRC_URI[md5sum] = "2c0cc6907aed5ea8005a8f332e34d92f"
SRC_URI[sha256sum] = "f68df996e0e699382b935bb4783dd402c301377df18f57e28e0318c4b3bff6da"

S = "${WORKDIR}/gstreamer-${PV}"
