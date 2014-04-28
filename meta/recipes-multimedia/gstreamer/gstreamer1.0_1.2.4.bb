include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "74d8f674187a5f0ddf38da594c3998c3"
SRC_URI[sha256sum] = "1e7ca67a7870a82c9ed51d51d0008cdbc550c41d64cc3ff3f9a1c2fc311b4929"
S = "${WORKDIR}/gstreamer-${PV}"

