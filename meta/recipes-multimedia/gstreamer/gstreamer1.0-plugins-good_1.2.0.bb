include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=a4e1830fce078028c8f0974161272607 \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRC_URI[md5sum] = "df96825d4154940fd934aa0a95b40836"
SRC_URI[sha256sum] = "2256a6b2744ea18d8810642cf9061a2f12f4b7eba87d3fbc98004262b4f0fdfa"

S = "${WORKDIR}/gst-plugins-good-${PV}"

