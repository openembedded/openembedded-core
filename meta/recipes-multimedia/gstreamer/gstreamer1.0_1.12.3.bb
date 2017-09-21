require gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
"
SRC_URI[md5sum] = "33dfcb690304fccdaff178440de13334"
SRC_URI[sha256sum] = "d388f492440897f02b01eebb033ca2d41078a3d85c0eddc030cdea5a337a216e"

S = "${WORKDIR}/gstreamer-${PV}"
