require gstreamer1.0-plugins.inc

LICENSE = "GPLv2+ & LGPLv2.1+"

# libid3tag
DEPENDS += "gstreamer1.0-plugins-base gconf zlib bzip2"

PR = "r1"

inherit gettext gconf


PACKAGECONFIG ??= " \
    ${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
    ${@base_contains('DISTRO_FEATURES', 'pulseaudio', 'pulseaudio', '', d)} \
    cairo flac gdk-pixbuf jpeg libpng soup speex taglib \
    "

X11DEPENDS = "virtual/libx11 libsm libxrender"
X11ENABLEOPTS = "--enable-x --enable-xvideo --enable-xshm"
X11DISABLEOPTS = "--disable-x --disable-xvideo --disable-xshm"
PACKAGECONFIG[x11]        = "${X11ENABLEOPTS},${X11DISABLEOPTS},${X11DEPENDS}"
PACKAGECONFIG[pulseaudio] = "--enable-pulse,--disable-pulse,pulseaudio"
PACKAGECONFIG[cairo]      = "--enable-cairo,--disable-cairo,cairo"
PACKAGECONFIG[flac]       = "--enable-flac,--disable-flac,flac"
PACKAGECONFIG[gdk-pixbuf] = "--enable-gdk_pixbuf,--disable-gdk_pixbuf,gdk-pixbuf"
PACKAGECONFIG[jack]       = "--enable-jack,--disable-jack,jack"
PACKAGECONFIG[jpeg]       = "--enable-jpeg,--disable-jpeg,jpeg"
PACKAGECONFIG[libpng]     = "--enable-libpng,--disable-libpng,libpng"
PACKAGECONFIG[soup]       = "--enable-soup,--disable-soup,libsoup-2.4"
PACKAGECONFIG[speex]      = "--enable-speex,--disable-speex,speex"
PACKAGECONFIG[taglib]     = "--enable-taglib,--disable-taglib,taglib"
PACKAGECONFIG[vpx]        = "--enable-vpx,--disable-vpx,libvpx"
PACKAGECONFIG[wavpack]    = "--enable-wavpack,--disable-wavpack,wavpack"
PACKAGECONFIG[v4l]        = "--with-libv4l2,--without-libv4l2,libv4l"

EXTRA_OECONF += " \
    --disable-directsound \
    --disable-waveform \
    --disable-oss \
    --disable-oss4 \
    --disable-sunaudio \
    --disable-osx_audio \
    --disable-osx_video \
    --disable-aalib \
    --disable-libcaca \
    --disable-libdv \
    --disable-dv1394 \
    --disable-shout2 \
    --disable-examples \
    ${GSTREAMER_1_0_ORC} \
"

FILES_${PN}-gconfelements += "${sysconfdir}/gconf/schemas/gstreamer-1.0.schemas"

