include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"
SRC_URI[md5sum] = "16c2050716383926909664aa6c6aca2b"
SRC_URI[sha256sum] = "984c133ec9d5d705e313d9e2bbd1472b93c6567460602a9a316578925ffe2eca"
S = "${WORKDIR}/gst-plugins-bad-${PV}"

