include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://gst/gst.h;beginline=1;endline=21;md5=8e5fe5e87d33a04479fde862e238eaa4"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "8f6066a37c71a0d0ff5fe5f7687fea12"
SRC_URI[sha256sum] = "68cada7ee24ede23e15dc81ccde11898eed1a7a3c6a2d81a8c31596fccb1b5ce"
S = "${WORKDIR}/gstreamer-${PV}"

