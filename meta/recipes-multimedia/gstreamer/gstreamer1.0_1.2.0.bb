include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "250b4bec48b0986103f5aab75e43cef9"
SRC_URI[sha256sum] = "94af5274299f845adf41cc504e0209b269acab7721293f49850fea27b4099463"
S = "${WORKDIR}/gstreamer-${PV}"

