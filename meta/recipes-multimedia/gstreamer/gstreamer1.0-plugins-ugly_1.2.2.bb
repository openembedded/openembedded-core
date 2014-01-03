include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "

SRC_URI[md5sum] = "32f57ac91369bbb3299469cc418507d0"
SRC_URI[sha256sum] = "4b6aac272a5be0d68f365ef6fba0f829fc5c1d1d601bb4dd9e85f5289b2b56c3"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"

