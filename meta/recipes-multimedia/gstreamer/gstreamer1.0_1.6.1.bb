include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    file://fix-install-hook.patch \
"
SRC_URI[md5sum] = "e72e2dc2ee06bfc045bb6010c89de520"
SRC_URI[sha256sum] = "973a3f213c8d41d6dd0e4e7e38fd6cccacd5ae1ac09e1179a8d5d869ef0a5c9c"

S = "${WORKDIR}/gstreamer-${PV}"

