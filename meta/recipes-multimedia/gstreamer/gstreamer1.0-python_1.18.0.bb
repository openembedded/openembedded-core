SUMMARY = "Python bindings for GStreamer 1.0"
HOMEPAGE = "http://cgit.freedesktop.org/gstreamer/gst-python/"
SECTION = "multimedia"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=c34deae4e395ca07e725ab0076a5f740"

SRC_URI = "https://gstreamer.freedesktop.org/src/${PNREAL}/${PNREAL}-${PV}.tar.xz"
SRC_URI[md5sum] = "2d55c22fcd57cae4fad55a07019aa334"
SRC_URI[sha256sum] = "76bfe8b85a9c4a6ddfb81874f2635fd0da38c3f39d9d2a0b175213218516dd45"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base python3-pygobject"
RDEPENDS_${PN} += "gstreamer1.0 gstreamer1.0-plugins-base python3-pygobject"

PNREAL = "gst-python"

S = "${WORKDIR}/${PNREAL}-${PV}"

# gobject-introspection is mandatory and cannot be configured
REQUIRED_DISTRO_FEATURES = "gobject-introspection-data"
UNKNOWN_CONFIGURE_WHITELIST_append = " introspection"

inherit meson pkgconfig distutils3-base upstream-version-is-even gobject-introspection features_check
