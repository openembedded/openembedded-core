include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "
SRC_URI[md5sum] = "558146cb5ec8b313afe2113aafc3da85"
SRC_URI[sha256sum] = "5314bb60f13d1a7b9c6317df73813af5f3f15a62c7c186b816b0024b5c61744d"
S = "${WORKDIR}/gst-plugins-ugly-${PV}"

