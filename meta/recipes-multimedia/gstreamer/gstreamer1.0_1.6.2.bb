include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
"
SRC_URI[md5sum] = "5e610b5a94c209487310739b39b6c464"
SRC_URI[sha256sum] = "5896716bd8e089dba452932a2eff2bb6f6c9d58ff64a96635d157f1ffaf8feb2"

S = "${WORKDIR}/gstreamer-${PV}"

