include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
"

SRC_URI[md5sum] = "6846d7289ec323c38c49b818171e955a"
SRC_URI[sha256sum] = "947a314a212b5d94985d89b43440dbe66b696e12bbdf9a2f78967b98d74abedc"

S = "${WORKDIR}/gstreamer-${PV}"
