DESCRIPTION = "FFmpeg-based GStreamer plug-in"
SECTION = "multimedia"
PRIORITY = "optional"
LICENSE = "LGPLv2+ & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://gst-libs/ext/ffmpeg/libavcodec/libpostproc/postprocess.h;beginline=1;endline=17;md5=a0c398349079dbd0ce70f03de8bc193a \
                    file://gst-libs/ext/ffmpeg/ffserver.c;beginlin=1;endline=18;md5=49bde48d6d5c631f6f49d74ed491ea08"
HOMEPAGE = "http://www.gstreamer.net/"
DEPENDS = "gstreamer zlib gst-plugins-base"
PR = "r4"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/${PN}/${PN}-${PV}.tar.bz2 \
           file://no_beos.patch;patch=1 \
           file://configure_fix.patch;patch=1"

FILES_${PN} += "${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dbg += "${libdir}/gstreamer-0.10/.debug"
FILES_${PN}-dev += "${libdir}/gstreamer-0.10/*.la ${libdir}/gstreamer-0.10/*.a"

EXTRA_OECONF = "--disable-sdltest --disable-ffplay --disable-freetypetest \
		--disable-vorbis --disable-vorbistest --disable-encoders \
		--disable-v4l --disable-audio-oss --disable-dv1394 \
		--disable-vhook --disable-ffmpeg --disable-ffserver \
		--enable-pp --disable-decoder-vorbis --with-pic=no --disable-altivec"

# We do this because the install program is called with -s which causes it to
# call "strip" and it then mangles cross compiled stuff..
PATH_prepend="${STAGING_DIR_NATIVE}${prefix_native}/${TARGET_SYS}/bin:"

# Hack to get STAGING_LIBDIR into the linker path when building ffmpeg
CC = "${CCACHE} ${HOST_PREFIX}gcc -L${STAGING_LIBDIR}"

acpaths = "-I ${S}/common/m4"
