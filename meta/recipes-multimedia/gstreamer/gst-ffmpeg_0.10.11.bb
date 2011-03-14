DESCRIPTION = "FFmpeg-based GStreamer plug-in"
SECTION = "multimedia"
PRIORITY = "optional"
LICENSE = "GPLv2+ & LGPLv2+ & ( (GPLv2+ & LGPLv2.1+) | (GPLv3+ & LGPLv3+) )"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://ext/libpostproc/gstpostproc.c;beginline=1;endline=18;md5=5896e445e41681324381f5869ee33d38 \
                    file://COPYING.LIB;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://ext/ffmpeg/gstffmpeg.h;beginline=1;endline=18;md5=ff65467b0c53cdfa98d0684c1bc240a9 \
                    file://gst-libs/ext/ffmpeg/LICENSE;md5=524b5ee4bdbbf755ebbb76727ba80b0d \
                    file://gst-libs/ext/ffmpeg/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://gst-libs/ext/ffmpeg/COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://gst-libs/ext/ffmpeg/COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://gst-libs/ext/ffmpeg/COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"
HOMEPAGE = "http://www.gstreamer.net/"
DEPENDS = "gstreamer gst-plugins-base zlib"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://lower-rank.diff \
"

SRC_URI[md5sum] = "0d23197ba7ac06ea34fa66d38469ebe5"
SRC_URI[sha256sum] = "ff36a138e5af4ed8dcc459d6d6521fe66ed31ec29ba9a924dc3675c6749a692e"

PR = "r0"

EXTRA_OECONF = "--with-ffmpeg-extra-configure=\"--target-os=linux\" "

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la ${libdir}/gstreamer-0.10/*.a"

# Hack to get STAGING_LIBDIR into the linker path when building ffmpeg
CC = "${CCACHE} ${HOST_PREFIX}gcc ${TARGET_CC_ARCH} -L${STAGING_LIBDIR}"
