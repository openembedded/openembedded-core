include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"

SRC_URI[md5sum] = "4fd078e1b9d903d22b67872b616f1715"
SRC_URI[sha256sum] = "a12fac6c106a7e4ae8bb2c7da508688d7db532b818319df2202f497cbd930afa"

SRC_URI += "file://bluez-fix-compilation-on-big-endian-systems.patch"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

