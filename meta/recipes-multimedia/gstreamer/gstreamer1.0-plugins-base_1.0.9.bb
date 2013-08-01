include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=622921ffad8cb18ab906c56052788a3f \
                    file://COPYING.LIB;md5=55ca817ccb7d5b5b66355690e9abc605 \
                   "
SRC_URI[md5sum] = "24af1986581d9c2cd3dc834ab64d133d"
SRC_URI[sha256sum] = "963e3b83d651661f495ca2e44ccd2d5c61e986e9d7706246e568276689a372ea"

S = "${WORKDIR}/gst-plugins-base-${PV}"
