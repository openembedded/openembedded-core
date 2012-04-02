DESCRIPTION = "FFmpeg-based GStreamer plug-in"
SECTION = "multimedia"
LICENSE = "GPLv2+ & LGPLv2+ & ( (GPLv2+ & LGPLv2.1+) | (GPLv3+ & LGPLv3+) )"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://ext/libpostproc/gstpostproc.c;beginline=1;endline=18;md5=5896e445e41681324381f5869ee33d38 \
                    file://COPYING.LIB;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://ext/ffmpeg/gstffmpeg.h;beginline=1;endline=18;md5=ff65467b0c53cdfa98d0684c1bc240a9 \
                    file://gst-libs/ext/libav/LICENSE;md5=abc3b8cb02856aa7823bbbd162d16232 \
                    file://gst-libs/ext/libav/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://gst-libs/ext/libav/COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://gst-libs/ext/libav/COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://gst-libs/ext/libav/COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"
HOMEPAGE = "http://www.gstreamer.net/"
DEPENDS = "gstreamer gst-plugins-base zlib"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://lower-rank.diff \
           file://configure-fix.patch \
           file://h264_qpel_mmx.patch \
"

SRC_URI[md5sum] = "7f5beacaf1312db2db30a026b36888c4"
SRC_URI[sha256sum] = "76fca05b08e00134e3cb92fa347507f42cbd48ddb08ed3343a912def187fbb62"

PR = "r3"

GSTREAMER_DEBUG ?= "--disable-debug"
EXTRA_OECONF = "--with-ffmpeg-extra-configure=\"--target-os=linux\" ${GSTREAMER_DEBUG}"

# yasm not found, use --disable-yasm for a crippled build for libav
EXTRA_OECONF_append_x86-64 = " --disable-yasm "
EXTRA_OECONF_append_x86 = " --disable-yasm "

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la"
FILES_${PN}-staticdev += "${libdir}/gstreamer-0.10/*.a"
