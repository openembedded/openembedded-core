SUMMARY = "GStreamer integration library for Clutter"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2+"

inherit clutter

SRC_URI += "file://enable-tests.patch"

DEPENDS = "gst-plugins-base gst-plugins-bad clutter-1.0"
RDEPENDS_${PN} += "gst-meta-base"
PACKAGES  =+ "${PN}-examples"

EXTRA_OECONF += "--disable-introspection"

FILES_${PN}          += "${libdir}/gstreamer-0.10/lib*.so"
FILES_${PN}-dev      += "${libdir}/gstreamer-0.10/*.la"
FILES_${PN}-dbg      += "${libdir}/gstreamer-0.10/.debug/lib*.so"
FILES_${PN}-examples  = "${bindir}/video-player ${bindir}/video-sink"
