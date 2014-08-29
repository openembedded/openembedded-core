include gstreamer1.0-plugins-bad.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=73a5855a8119deb017f5f13cf327095d \
                    file://gst/tta/filters.h;beginline=12;endline=29;md5=8a08270656f2f8ad7bb3655b83138e5a \
                    file://COPYING.LIB;md5=21682e4e8fea52413fd26c60acb907e5 \
                    file://gst/tta/crc32.h;beginline=12;endline=29;md5=27db269c575d1e5317fffca2d33b3b50"

SRC_URI += "file://0001-gl-do-not-check-for-GL-GLU-EGL-GLES2-libs-if-disable.patch"

SRC_URI[md5sum] = "20cb190b18dc63017326321cdb7c91e5"
SRC_URI[sha256sum] = "0268db2faaf0bb22e5b709a11633abbca4f3d289b1f513bb262d0bf3f53e19ae"
S = "${WORKDIR}/gst-plugins-bad-${PV}"

