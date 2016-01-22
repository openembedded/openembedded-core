require gstreamer1.0-plugins.inc

LICENSE = "GPLv2+ & LGPLv2+"

DEPENDS += "freetype iso-codes util-linux zlib"

inherit gettext

PACKAGES_DYNAMIC =+ "^libgst.*"

PACKAGECONFIG ??= " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    gio-unix-2.0 ivorbis ogg orc pango theora vorbis \
"

X11DEPENDS = "virtual/libx11 libsm libxrender libxv"
X11ENABLEOPTS = "--enable-x --enable-xvideo --enable-xshm"
X11DISABLEOPTS = "--disable-x --disable-xvideo --disable-xshm"

PACKAGECONFIG[alsa]         = "--enable-alsa,--disable-alsa,alsa-lib"
PACKAGECONFIG[cdparanoia]   = "--enable-cdparanoia,--disable-cdparanoia,cdparanoia"
PACKAGECONFIG[gio-unix-2.0] = "--enable-gio_unix_2_0,--disable-gio_unix_2_0,glib-2.0"
PACKAGECONFIG[ivorbis]      = "--enable-ivorbis,--disable-ivorbis,tremor"
PACKAGECONFIG[ogg]          = "--enable-ogg,--disable-ogg,libogg"
PACKAGECONFIG[pango]        = "--enable-pango,--disable-pango,pango"
PACKAGECONFIG[theora]       = "--enable-theora,--disable-theora,libtheora"
PACKAGECONFIG[visual]       = "--enable-libvisual,--disable-libvisual,libvisual"
PACKAGECONFIG[vorbis]       = "--enable-vorbis,--disable-vorbis,libvorbis"
PACKAGECONFIG[x11]          = "${X11ENABLEOPTS},${X11DISABLEOPTS},${X11DEPENDS}"

EXTRA_OECONF += " \
    --enable-zlib \
    --disable-freetypetest \
"

CACHED_CONFIGUREVARS_append_x86 = " ac_cv_header_emmintrin_h=no ac_cv_header_xmmintrin_h=no"

FILES_${MLPREFIX}libgsttag-1.0 += "${datadir}/gst-plugins-base/1.0/license-translations.dict"
