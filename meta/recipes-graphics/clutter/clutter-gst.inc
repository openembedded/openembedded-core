DESCRIPTION = "Clutter GStreamer"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPLv2+"

DEPENDS = "gstreamer gst-plugins-base"

FILESPATH = "${FILE_DIRNAME}/clutter-gst"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/video-player ${bindir}/video-sink"

inherit autotools pkgconfig gtk-doc

