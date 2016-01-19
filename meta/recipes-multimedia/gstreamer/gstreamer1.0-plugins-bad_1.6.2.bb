include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50 \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a"

SRC_URI += " \
    file://configure-allow-to-disable-libssh2.patch \
    file://0001-glimagesink-Downrank-to-marginal.patch \
"

SRC_URI[md5sum] = "7c73bec1d389f0e184ebbbbb9e9f883d"
SRC_URI[sha256sum] = "650855e39ff56a8bb6cb0c192109c5926ce12f536d06e19ebf829de71ef396fe"

S = "${WORKDIR}/gst-plugins-bad-${PV}"
