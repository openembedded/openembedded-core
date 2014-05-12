require gstreamer1.0-plugins.inc

LICENSE = "GPLv2+ & LGPLv2+"

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'virtual/libx11 libxv', '', d)}"
DEPENDS += "freetype liboil util-linux"

inherit gettext

PACKAGES_DYNAMIC =+ "^libgst.*"

PACKAGECONFIG ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} \
    orc ivorbis ogg theora vorbis \
    "

X11DEPENDS = "virtual/libx11 libsm libxrender"
X11ENABLEOPTS = "--enable-x --enable-xvideo --enable-xshm"
X11DISABLEOPTS = "--disable-x --disable-xvideo --disable-xshm"
PACKAGECONFIG[x11]     = "${X11ENABLEOPTS},${X11DISABLEOPTS},${X11DEPENDS}"
PACKAGECONFIG[alsa]    = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[ivorbis] = "--enable-ivorbis,--disable-ivorbis,tremor"
PACKAGECONFIG[ogg]     = "--enable-ogg,--disable-ogg,libogg"
PACKAGECONFIG[theora]  = "--enable-theora,--disable-theora,libtheora"
PACKAGECONFIG[vorbis]  = "--enable-vorbis,--disable-vorbis,libvorbis"
PACKAGECONFIG[pango]   = "--enable-pango,--disable-pango,pango"


# cdparanoia and libvisual do not seem to exist anywhere in OE
EXTRA_OECONF += " \
    --disable-freetypetest \
    --disable-cdparanoia \
    --disable-libvisual \
    ${GSTREAMER_1_0_ORC} \
"

FILES_${PN} += "${datadir}/gst-plugins-base"

CACHED_CONFIGUREVARS_append_i586 = " ac_cv_header_emmintrin_h=no ac_cv_header_xmmintrin_h=no"
