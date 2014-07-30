include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"
SRC_URI[md5sum] = "3bc0fcfe8d16ad1295f0454c1fcb4ba3"
SRC_URI[sha256sum] = "ff2cb754f7725b205aec66002b1406e440f3a03194b6cad2d126ef5cd00902f9"
S = "${WORKDIR}/gst-plugins-bad-${PV}"

