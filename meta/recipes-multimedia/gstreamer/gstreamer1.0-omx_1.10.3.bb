include gstreamer1.0-omx.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

SRC_URI = "http://gstreamer.freedesktop.org/src/gst-omx/gst-omx-${PV}.tar.xz"
SRC_URI[md5sum] = "1eead212796151d6ac08e091bacbd190"
SRC_URI[sha256sum] = "dd88451e175df7a6b6f619eff0f43a6078054ec7068787b19faf89af94cdb6e1"

S = "${WORKDIR}/gst-omx-${PV}"
