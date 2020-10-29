SUMMARY = "A library on top of GStreamer for building an RTSP server"
HOMEPAGE = "http://cgit.freedesktop.org/gstreamer/gst-rtsp-server/"
SECTION = "multimedia"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base"

PNREAL = "gst-rtsp-server"

SRC_URI = "https://gstreamer.freedesktop.org/src/${PNREAL}/${PNREAL}-${PV}.tar.xz"

SRC_URI[md5sum] = "77fe28c702a83566811ab5c7bbe99ab2"
SRC_URI[sha256sum] = "2ad19311054cbf2df0d0622936bc703dedc06ced706df46a3d3a3ea5a4b7c70f"

S = "${WORKDIR}/${PNREAL}-${PV}"

inherit meson pkgconfig upstream-version-is-even gobject-introspection

EXTRA_OEMESON += " \
    -Ddoc=disabled \
    -Dexamples=disabled \
    -Dtests=disabled \
"

GIR_MESON_ENABLE_FLAG = "enabled"
GIR_MESON_DISABLE_FLAG = "disabled"

# Starting with 1.8.0 gst-rtsp-server includes dependency-less plugins as well
require gstreamer1.0-plugins-packaging.inc
