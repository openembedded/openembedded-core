include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"

SRC_URI += "file://0001-glimagesink-Downrank-to-marginal.patch"

SRC_URI[md5sum] = "c92d7d32de68e7293712bf9b6c99dc77"
SRC_URI[sha256sum] = "e176a9af125f6874b3d6724aa7566a198fa225d3ece0a7ac2f2b51c57e525466"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

