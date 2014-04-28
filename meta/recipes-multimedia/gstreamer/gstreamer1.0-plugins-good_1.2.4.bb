include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"
SRC_URI[md5sum] = "8aac024ee0cd98b67a3066ad31d8c677"
SRC_URI[sha256sum] = "c9c90368393c2e5e78387e95c02ce7b19f48e793bba6d8547f2c4b51c6f420d3"
S = "${WORKDIR}/gst-plugins-good-${PV}"

