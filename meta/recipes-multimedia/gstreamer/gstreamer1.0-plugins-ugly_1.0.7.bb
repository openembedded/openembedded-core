include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

SRC_URI[md5sum] = "8754edf6c3be235f232fb75ad11708bb"
SRC_URI[sha256sum] = "b78b8cfabe322497da432a0f297dbb21862a033f95e8d4cd8f207eccb5288f2b"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"

