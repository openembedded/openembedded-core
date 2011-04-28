DESCRIPTION = "Music player"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://main.c;beginline=1;endline=20;md5=0c02b4ef945956832b37a036b9cc103a"
DEPENDS = "gtk+ gstreamer libowl-av"

RDEPENDS_${PN} = "gst-plugins-base-audioconvert \
            gst-plugins-base-audioresample \
            gst-plugins-base-typefindfunctions \
            gst-plugins-base-playbin"

RRECOMMENDS_${PN} = "gst-plugins-good-id3demux \
               gst-plugins-base-vorbis \
               gst-plugins-base-alsa \
               gst-plugins-base-ogg \
               ${COMMERCIAL_AUDIO_PLUGINS}"

SRCREV = "399"
PV = "0.0+svnr${SRCPV}"

PR = "r4"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${BPN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
