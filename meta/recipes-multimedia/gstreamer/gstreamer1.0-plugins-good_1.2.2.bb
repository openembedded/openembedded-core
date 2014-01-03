include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRC_URI[md5sum] = "f8a9be6c5362d13ee41b600c74e843f4"
SRC_URI[sha256sum] = "6c090f00e8e4588f12807bd9fbb06a03b84a512c93e84d928123ee4a42228a81"
S = "${WORKDIR}/gst-plugins-good-${PV}"

