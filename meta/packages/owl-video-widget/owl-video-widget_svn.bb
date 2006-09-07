LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "gtk+ gstreamer gst-plugins-base"
RDEPENDS = "gst-meta-base"
RRECOMMENDS = "gst-meta-audio gst-meta-video"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "OpenedHand Widget Library video widget"

PV = "0.1+svn${SRCDATE}"
S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

inherit autotools pkgconfig

