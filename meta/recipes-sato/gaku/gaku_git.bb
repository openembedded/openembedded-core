SUMMARY = "Simple music player using GTK+ and GStreamer"
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

SRCREV = "a0be2fe4b5f12b8b07f4e3bd624b3729657f0ac5"
PV = "0.1+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/${BPN}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
