include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"
SRC_URI[md5sum] = "8007d57a38f6b2882961b2547fa4597c"
SRC_URI[sha256sum] = "48a62e7987fffa289a091dfc8ccc80b401d110632b8fc1adce5b82fc092f2685"
S = "${WORKDIR}/gst-plugins-good-${PV}"

