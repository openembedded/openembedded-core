include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"
SRC_URI[md5sum] = "1a1f96bc27ad446e559474299160a9a8"
SRC_URI[sha256sum] = "bfb33536a515bdcc34482f64b8d9cc3e47c753878b254923b419bc2f7485e470"
S = "${WORKDIR}/gst-plugins-good-${PV}"

