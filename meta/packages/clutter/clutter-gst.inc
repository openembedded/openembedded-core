DESCRIPTION = "Clutter GStreamer"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "clutter-1.0 gstreamer gst-plugins-base"

FILESPATH = "${FILE_DIRNAME}/clutter-gst"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/video-player ${bindir}/video-sink ${bindir}/audio"

inherit autotools pkgconfig gtk-doc

