include gstreamer1.0-plugins-ugly.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://tests/check/elements/xingmux.c;beginline=1;endline=21;md5=4c771b8af188724855cb99cadd390068 "
SRC_URI[md5sum] = "316974af949ca4654efee704a0164076"
SRC_URI[sha256sum] = "25440435ac4ed795d213f2420a0e7355e4a2e2e76d1f9d020b2073f815e8b071"
S = "${WORKDIR}/gst-plugins-ugly-${PV}"

