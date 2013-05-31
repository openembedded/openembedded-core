require gstreamer1.0-plugins.inc

LICENSE = "GPLv2+ & LGPLv2+ & LGPLv2.1+ "

DEPENDS += "gstreamer1.0-plugins-base bzip2"

SRC_URI[md5sum] = "d1493d1219b836a8cbf54f4fba962420"
SRC_URI[sha256sum] = "5f49e6353fdc855834b5beb054b3a47ef5fa558006c7eda6d2ec07b36315c2ab"

PR = "r1"

S = "${WORKDIR}/gst-plugins-bad-${PV}"

inherit gettext


PACKAGECONFIG ??= " \
    curl eglgles wayland \
    "

PACKAGECONFIG[assrender] = "--enable-assrender,--disable-assrender,libass"
PACKAGECONFIG[curl]      = "--enable-curl,--disable-curl,curl"
PACKAGECONFIG[eglgles]   = "--enable-eglgles,--disable-eglgles,virtual/egl virtual/libgles2"
PACKAGECONFIG[faad]      = "--enable-faad,--disable-faad,faad2"
PACKAGECONFIG[libmms]    = "--enable-libmms,--disable-libmms,libmms"
PACKAGECONFIG[modplug]   = "--enable-modplug,--disable-modplug,libmodplug"
PACKAGECONFIG[mpg123]    = "--enable-mpg123,--disable-mpg123,mpg123"
PACKAGECONFIG[opus]      = "--enable-opus,--disable-opus,libopus"
PACKAGECONFIG[flite]     = "--enable-flite,--disable-flite,flite-alsa"
PACKAGECONFIG[opencv]    = "--enable-opencv,--disable-opencv,opencv"
PACKAGECONFIG[wayland]   = "--enable-wayland,--disable-wayland,wayland"



# these plugins have not been ported to 1.0 (yet):
#   directdraw vcd uvch264 apexsink cdaudio cog dc1394 directfb dirac fbdev jasper
#   kate ladspa lv2 linsys musepack musicbrainz mythtv nas neon ofa openal rsvg
#   timidity teletextdec wildmidi sdl sndfile xvid wininet acm gsettings sndio
#mfc hls openjpeg sbc 
EXTRA_OECONF += " \
    --enable-dvb \
    --enable-shm \
    --disable-uvch264 \
    --disable-vcd \
    --disable-fbdev \
    --disable-examples \
    --disable-experimental \
    --disable-celt \
    --disable-chromaprint \
    --disable-decklink \
    --disable-dts \
    --disable-faac \
    --disable-gme \
    --disable-gsm \
    --disable-mpeg2enc \
    --disable-mplex \
    --disable-resindvd \
    --disable-rtmp \
    --disable-schro \
    --disable-soundtouch \
    --disable-spandsp \
    --disable-vdpau \
    --disable-acm \
    --disable-android_media \
    --disable-apexsink \
    --disable-apple_media \
    --disable-avc \
    --disable-cdaudio \
    --disable-cog \
    --disable-dc1394 \
    --disable-dirac \
    --disable-direct3d \
    --disable-direct3d9 \
    --disable-directdraw \
    --disable-directsound \
    --disable-directshow \
    --disable-gsettings \
    --disable-kate \
    --disable-ladspa \
    --disable-linsys \
    --disable-lv2 \
    --disable-mimic \
    --disable-musepack \
    --disable-mythtv \
    --disable-nas \
    --disable-ofa \
    --disable-osx_video \
    --disable-pvr \
    --disable-quicktime \
    --disable-sndio \
    --disable-spc \
    --disable-swfdec \
    --disable-teletextdec \
    --disable-timidity \
    --disable-voaacenc \
    --disable-voamrwbenc \
    --disable-wildmidi \
    --disable-wininet \
    --disable-xvid \
    --disable-zbar \
    ${GSTREAMER_1_0_ORC} \
    "

ARM_INSTRUCTION_SET = "arm"

