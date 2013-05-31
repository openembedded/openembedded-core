include gstreamer1.0-plugins-good.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=622921ffad8cb18ab906c56052788a3f \
                    file://gst/replaygain/rganalysis.c;beginline=1;endline=23;md5=b60ebefd5b2f5a8e0cab6bfee391a5fe"

SRC_URI[md5sum] = "e4b1c825475a9b478fe29e8e9f34516f"
SRC_URI[sha256sum] = "a016a3b377c86658627aef902b9204108209100b2e88fcc3b695c392af1b4035"

S = "${WORKDIR}/gst-plugins-good-${PV}"

