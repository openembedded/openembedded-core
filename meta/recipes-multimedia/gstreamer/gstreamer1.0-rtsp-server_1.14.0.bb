SUMMARY = "A library on top of GStreamer for building an RTSP server"
HOMEPAGE = "http://cgit.freedesktop.org/gstreamer/gst-rtsp-server/"
SECTION = "multimedia"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6762ed442b3822387a51c92d928ead0d"

DEPENDS = "gstreamer1.0 gstreamer1.0-plugins-base"

PNREAL = "gst-rtsp-server"

SRC_URI = "http://gstreamer.freedesktop.org/src/${PNREAL}/${PNREAL}-${PV}.tar.xz \
           file://0001-Don-t-hardcode-libtool-name-when-using-introspection.patch \
           file://0001-introspection.m4-prefix-pkgconfig-paths-with-PKG_CON.patch \
           file://gtk-doc-tweaks.patch \
           "

SRC_URI[md5sum] = "8a505c88f7469c3a0d1e9f4e9a315e53"
SRC_URI[sha256sum] = "6b65a077bed815f6d3157ebea503cc9f3c32d289af2756b7ff7e3958744d9756"

S = "${WORKDIR}/${PNREAL}-${PV}"

inherit autotools pkgconfig upstream-version-is-even gobject-introspection gtk-doc

EXTRA_OECONF = "--disable-examples --disable-tests"

# Starting with 1.8.0 gst-rtsp-server includes dependency-less plugins as well
LIBV = "1.0"
require gst-plugins-package.inc

delete_pkg_m4_file() {
        # This m4 file is out of date and is missing PKG_CONFIG_SYSROOT_PATH tweaks which we need for introspection
        rm "${S}/common/m4/pkg.m4" || true
}

do_configure[prefuncs] += " delete_pkg_m4_file"
