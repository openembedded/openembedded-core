DESCRIPTION = "Clutter GStreamer"
HOMEPAGE = "http://www.clutter-project.org/"
LICENSE = "LGPL"

DEPENDS = "clutter gstreamer gst-plugins-base"

PACKAGES =+ "${PN}-examples"
FILES_${PN}-examples = "${bindir}/video-player ${bindir}/video-sink ${bindir}/audio"

inherit autotools pkgconfig

do_configure_prepend() {
        touch ${S}/gtk-doc.make
}

do_stage () {
        autotools_stage_all
}

