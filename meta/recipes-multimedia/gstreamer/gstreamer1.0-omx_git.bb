DEFAULT_PREFERENCE = "-1"

include gstreamer1.0-omx.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c \
                    file://omx/gstomx.h;beginline=1;endline=21;md5=5c8e1fca32704488e76d2ba9ddfa935f"

SRC_URI = " \
    git://anongit.freedesktop.org/gstreamer/gst-omx;branch=master \
    file://0001-omx-fixed-type-error-in-printf-call.patch \    
    "
S = "${WORKDIR}/git"

SRCREV = "a2db76b048db278ef0aa798e106b7594264e06c0"

do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}

