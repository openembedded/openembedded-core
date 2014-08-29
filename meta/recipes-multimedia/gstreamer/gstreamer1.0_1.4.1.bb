include gstreamer1.0.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d \
                    file://gst/gst.h;beginline=1;endline=21;md5=e059138481205ee2c6fc1c079c016d0d"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.xz \
    file://0001-Fix-crash-with-gst-inspect.patch \
"
SRC_URI[md5sum] = "bd0938d680d657249b885162f310702d"
SRC_URI[sha256sum] = "5638f75003282135815c0077d491da11e9a884ad91d4ba6ab3cc78bae0fb452e"
S = "${WORKDIR}/gstreamer-${PV}"

