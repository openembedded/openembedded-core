require gstreamer1.0-plugins-common.inc
require gstreamer1.0-plugins-license.inc

SUMMARY = "'Ugly GStreamer plugins"
HOMEPAGE = "https://gstreamer.freedesktop.org/"
BUGTRACKER = "https://gitlab.freedesktop.org/gstreamer/gst-plugins-ugly/-/issues"

LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                   "

LICENSE = "LGPL-2.1-or-later & GPL-2.0-or-later"
LICENSE_FLAGS = "commercial"

SRC_URI = "https://gstreamer.freedesktop.org/src/gst-plugins-ugly/gst-plugins-ugly-${PV}.tar.xz"
SRC_URI[sha256sum] = "8f30f44db0bd063709bf6fbe55138e3a98af0abcb61c360f35582bbe10e80691"

S = "${WORKDIR}/gst-plugins-ugly-${PV}"

DEPENDS += "gstreamer1.0-plugins-base"

GST_PLUGIN_SET_HAS_EXAMPLES = "0"

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    a52dec mpeg2dec \
"

PACKAGECONFIG[amrnb]    = "-Damrnb=enabled,-Damrnb=disabled,opencore-amr"
PACKAGECONFIG[amrwb]    = "-Damrwbdec=enabled,-Damrwbdec=disabled,opencore-amr"
PACKAGECONFIG[a52dec]   = "-Da52dec=enabled,-Da52dec=disabled,liba52"
PACKAGECONFIG[cdio]     = "-Dcdio=enabled,-Dcdio=disabled,libcdio"
PACKAGECONFIG[dvdread]  = "-Ddvdread=enabled,-Ddvdread=disabled,libdvdread"
PACKAGECONFIG[mpeg2dec] = "-Dmpeg2dec=enabled,-Dmpeg2dec=disabled,mpeg2dec"
PACKAGECONFIG[x264]     = "-Dx264=enabled,-Dx264=disabled,x264"

GSTREAMER_GPL = "${@bb.utils.filter('PACKAGECONFIG', 'a52dec cdio dvdread mpeg2dec x264', d)}"

EXTRA_OEMESON += " \
    -Ddoc=disabled \
    -Dsidplay=disabled \
"

FILES:${PN}-amrnb += "${datadir}/gstreamer-1.0/presets/GstAmrnbEnc.prs"
FILES:${PN}-x264 += "${datadir}/gstreamer-1.0/presets/GstX264Enc.prs"
