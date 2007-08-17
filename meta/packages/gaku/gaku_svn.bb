DESCRIPTION = "Music player"
LICENSE = "GPL"
DEPENDS = "gtk+ gstreamer"
PV = "0.0+svnr${SRCREV}"

RDEPENDS = "gst-plugin-audioconvert \
        gst-plugin-audioresample \
        gst-plugin-alsa \
        gst-plugin-typefindfunctions \
        gst-plugin-id3demux \
        gst-plugin-playbin"
RRECOMMENDS = "gst-plugin-mad gst-plugin-ivorbis gst-plugin-ogg"

S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

inherit autotools pkgconfig
