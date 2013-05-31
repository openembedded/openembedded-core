include gstreamer1.0-plugins-base.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3 \
                    file://common/coverage/coverage-report.pl;beginline=2;endline=17;md5=622921ffad8cb18ab906c56052788a3f \
                    file://COPYING.LIB;md5=55ca817ccb7d5b5b66355690e9abc605 \
                   "

SRC_URI[md5sum] = "b5b43cfbf82b413ce2e07a190d87e68f"
SRC_URI[sha256sum] = "014805e50b696bc06c3862ea656df079fc0b5fef0c10f16e9f085f290545677a"

S = "${WORKDIR}/gst-plugins-base-${PV}"
