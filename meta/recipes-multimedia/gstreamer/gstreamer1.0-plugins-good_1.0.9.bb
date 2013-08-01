include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=622921ffad8cb18ab906c56052788a3f \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRC_URI[md5sum] = "20bb77f201b044f4ba61de167c4466d7"
SRC_URI[sha256sum] = "cfa2e617a76f93e9ddd4ae1109297e93fb4a06b152042b996231234a72c5a5ff"

S = "${WORKDIR}/gst-plugins-good-${PV}"

