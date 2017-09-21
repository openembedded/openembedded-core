include gstreamer1.0-omx.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"

SRC_URI[md5sum] = "53d2ca9739f9189d9c1924d4af71e8a4"
SRC_URI[sha256sum] = "eef5de8bab1bb495bfbc9d16af9837d7f55b47cb6b97819b3152c5899c85843c"

S = "${WORKDIR}/gst-omx-${PV}"
