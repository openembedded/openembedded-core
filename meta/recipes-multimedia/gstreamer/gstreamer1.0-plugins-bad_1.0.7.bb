include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=71a904d99ce7ae0c1cf129891b98145c"

SRC_URI[md5sum] = "d1493d1219b836a8cbf54f4fba962420"
SRC_URI[sha256sum] = "5f49e6353fdc855834b5beb054b3a47ef5fa558006c7eda6d2ec07b36315c2ab"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

