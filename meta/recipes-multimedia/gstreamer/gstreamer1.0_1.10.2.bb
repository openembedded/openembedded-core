require gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://deterministic-unwind.patch \
"
SRC_URI[md5sum] = "0d289e5bcec6353e6540ddb75b7d371b"
SRC_URI[sha256sum] = "150e8e81febac94c161d8141cde78a38038a8f56e8ec549f353da54994278d65"

S = "${WORKDIR}/gstreamer-${PV}"
