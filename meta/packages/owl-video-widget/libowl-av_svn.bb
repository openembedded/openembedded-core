DESCRIPTION = "OpenedHand Widget Library Audio/Video"
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "gtk+ gstreamer gst-plugins-base"
RDEPENDS = "gst-meta-base"
RRECOMMENDS = "gst-meta-audio gst-meta-video"

PV = "0.0+svnr${SRCREV}"
PR = "r2"

S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

inherit autotools pkgconfig

do_stage () {
	autotools_stage_all
}