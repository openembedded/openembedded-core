include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "8155b9c7574ccaa361cc504e8e0e72dc"
SRC_URI[sha256sum] = "0f9a9817a384b3448c368c23345e5122435caef9c00f1c40d7b1953827b0d8eb"
S = "${WORKDIR}/gstreamer-${PV}"

