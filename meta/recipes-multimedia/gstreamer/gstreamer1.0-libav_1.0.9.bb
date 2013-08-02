include gstreamer1.0-libav.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=55ca817ccb7d5b5b66355690e9abc605 \
                    file://ext/libav/gstav.h;beginline=1;endline=18;md5=ff65467b0c53cdfa98d0684c1bc240a9 \
                    file://gst-libs/ext/libav/LICENSE;md5=abc3b8cb02856aa7823bbbd162d16232 \
                    file://gst-libs/ext/libav/COPYING.GPLv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://gst-libs/ext/libav/COPYING.GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://gst-libs/ext/libav/COPYING.LGPLv2.1;md5=e344c8fa836c3a41c4cbd79d7bd3a379 \
                    file://gst-libs/ext/libav/COPYING.LGPLv3;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI = " \
    http://gstreamer.freedesktop.org/src/gst-libav/gst-libav-${PV}.tar.xz \
    file://0001-Disable-yasm-for-libav-when-disable-yasm.patch \
    file://libav_e500mc.patch \
    "
SRC_URI[md5sum] = "8414f8d4c4a239a74a3d5eadf3d14875"
SRC_URI[sha256sum] = "759641c0597c24191322f40945b363b75df299a539ff4086650be6193028189a"

LIBAV_EXTRA_CONFIGURE_COMMON_ARG = "--target-os=linux \
  --cc='${CC}' --as='${CC}' --ld='${CC}' --nm='${NM}' --ar='${AR}' \
  --ranlib='${RANLIB}' \
  ${GSTREAMER_1_0_DEBUG}"

S = "${WORKDIR}/gst-libav-${PV}"

