include gstreamer1.0-omx.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"

SRC_URI[md5sum] = "bb34b5742223267298bcffc209104a92"
SRC_URI[sha256sum] = "7a1d8d28d70dacc6bd3c7ee7d7e40df6d5a1d38d7c256d5c9c5c8ef15c005014"

S = "${WORKDIR}/gst-omx-${PV}"

