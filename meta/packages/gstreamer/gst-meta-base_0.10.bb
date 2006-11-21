DESCRIPTION = "Gstreamer package groups"
DEPENDS = "gstreamer gst-plugins-base gst-plugins-good gst-plugins-bad gst-plugins-ugly"
PR = "r3"

PACKAGES = "\
    gst-meta-base \
    gst-meta-audio \
    gst-meta-debug \
    gst-meta-video"

ALLOW_EMPTY = "1"

RDEPENDS_gst-meta-base = "\
    gstreamer \
    gst-plugin-playbin \
    gst-plugin-decodebin \
    gst-plugin-gnomevfs \
    gst-plugin-alsa \
    gst-plugin-volume \
    gst-plugin-ximagesink \
    gst-plugin-audioconvert \
    gst-plugin-audioresample \
    gst-plugin-typefindfunctions \
    gst-plugin-videoscale \
    gst-plugin-ffmpegcolorspace \
    gst-plugin-autodetect"


RDEPENDS_gst-meta-audio = "\
    gst-meta-base \
    gst-plugin-ivorbis \
    gst-plugin-ogg \
    gst-plugin-mad"


RDEPENDS_gst-meta-debug = "\
    gst-meta-base \
    gst-plugin-debug \
    gst-plugin-audiotestsrc \
    gst-plugin-videotestsrc"


RDEPENDS_gst-meta-video = "\
    gst-meta-base \
    gst-plugin-avi \
    gst-plugin-matroska \
    gst-plugin-mpeg2dec"

RRECOMMENDS_gst-meta-video = "\
    gst-meta-audio"
