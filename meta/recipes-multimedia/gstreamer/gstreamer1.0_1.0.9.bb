include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://gst/gst.h;beginline=1;endline=21;md5=8e5fe5e87d33a04479fde862e238eaa4"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
    "
SRC_URI[md5sum] = "cbbad73d703b811bc8bd18e47b36e534"
SRC_URI[sha256sum] = "17a2c60a82baa461ef685ad3de187edb9c03a2c7e07513daba58a5a32efacaa3"
S = "${WORKDIR}/gstreamer-${PV}"

