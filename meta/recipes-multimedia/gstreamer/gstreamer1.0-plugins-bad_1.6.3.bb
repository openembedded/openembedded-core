include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50 \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz \
    file://configure-allow-to-disable-libssh2.patch \
    file://0001-glimagesink-Downrank-to-marginal.patch \
"

SRC_URI[md5sum] = "4857adcafe41e4b9b8805cf88303bd55"
SRC_URI[sha256sum] = "971b29101d6a9c5e3fe94d99d977a227f58f0b2d29b6ca2c7f292052542b3a61"

S = "${WORKDIR}/gst-plugins-bad-${PV}"
