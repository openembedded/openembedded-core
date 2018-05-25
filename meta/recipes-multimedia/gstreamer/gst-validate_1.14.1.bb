SUMMARY = "Gstreamer validation tool"
DESCRIPTION = "A Tool to test GStreamer components"
HOMEPAGE = "https://gstreamer.freedesktop.org/releases/gst-validate/1.12.3.html"
SECTION = "multimedia"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

SRC_URI = "https://gstreamer.freedesktop.org/src/${BPN}/${BP}.tar.xz \
           "
SRC_URI[md5sum] = "293262badee41357e029e5f2664354d7"
SRC_URI[sha256sum] = "68e3f38f66e0d04837b60090fe5d9ca1127f5df124669f34ca82e241209171e4"

DEPENDS = "json-glib glib-2.0 glib-2.0-native gstreamer1.0 gstreamer1.0-plugins-base"
RRECOMMENDS_${PN} = "git"

FILES_${PN} += "${datadir}/gstreamer-1.0/* ${libdir}/gst-validate-launcher/* ${libdir}/gstreamer-1.0/*"

inherit pkgconfig gettext autotools gobject-introspection gtk-doc upstream-version-is-even

# With gtk-doc enabled this recipe fails to build, so forcibly disable it:
# WORKDIR/build/docs/validate/gst-validate-scan: line 117:
# WORKDIR/build/docs/validate/.libs/lt-gst-validate-scan: No such file or directory
GTKDOC_ENABLED = "False"
