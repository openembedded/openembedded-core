include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=71a904d99ce7ae0c1cf129891b98145c"

SRC_URI[md5sum] = "569e5122fd7bfd7bd861a537f0a28c60"
SRC_URI[sha256sum] = "69d236b1d8188270a3f51f6710146d0ca63c2f1a9f6cfbab3399ef01b9498f75"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

