require gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://deterministic-unwind.patch \
"
SRC_URI[md5sum] = "8c1806916107f79e88bc6ab5f6f7754e"
SRC_URI[sha256sum] = "85b9dc1b2991f224fa90d534ec57014896c479e061dc9fa1bc16ae17cbebb63d"

S = "${WORKDIR}/gstreamer-${PV}"
