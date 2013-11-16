include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

SRC_URI[md5sum] = "81c97981ed373bd77bb10f2ae555c166"
SRC_URI[sha256sum] = "e4760af4b12bf97ba0a8001cfe733d9d52160a0ad81f6c6f0d0d3a9e798626de"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"

