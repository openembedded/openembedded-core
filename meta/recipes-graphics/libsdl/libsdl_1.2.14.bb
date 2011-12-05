SUMMARY = "Simple DirectMedia Layer"
DESCRIPTION = "Simple DirectMedia Layer is a cross-platform multimedia \
library designed to provide low level access to audio, keyboard, mouse, \
joystick, 3D hardware via OpenGL, and 2D video framebuffer."
HOMEPAGE = "http://www.libsdl.org"
BUGTRACKER = "http://bugzilla.libsdl.org/"

SECTION = "libs"

LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=27818cd7fd83877a8e3ef82b82798ef4"

PROVIDES = "virtual/libsdl"

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)} virtual/libx11 libxext libxrandr libxrender tslib"
DEPENDS_virtclass-nativesdk = "libx11-nativesdk libxrandr-nativesdk libxrender-nativesdk libxext-nativesdk"

PR = "r2"

SRC_URI = "http://www.libsdl.org/release/SDL-${PV}.tar.gz \
           file://configure_tweak.patch \
       "

S = "${WORKDIR}/SDL-${PV}"

SRC_URI[md5sum] = "e52086d1b508fa0b76c52ee30b55bec4"
SRC_URI[sha256sum] = "5d927e287034cb6bb0ebccfa382cb1d185cb113c8ab5115a0759798642eed9b6"

inherit autotools binconfig pkgconfig

EXTRA_OECONF = "--disable-static --disable-debug --enable-cdrom --enable-threads --enable-timers --enable-endian \
                --enable-file --disable-oss --disable-esd --disable-arts \
                --disable-diskaudio --disable-nas --disable-esd-shared --disable-esdtest \
                --disable-mintaudio --disable-nasm --enable-video-x11 --disable-video-dga \
                --disable-video-fbcon --disable-video-directfb --disable-video-ps2gs --disable-video-ps3 \
                --disable-video-xbios --disable-video-gem --disable-video-dummy \
                --enable-input-events --enable-input-tslib --enable-pthreads \
                ${@base_contains('DISTRO_FEATURES', 'opengl', '--enable-video-opengl', '--disable-video-opengl', d)} \
                --disable-video-svga \
                --disable-video-picogui --disable-video-qtopia --enable-dlopen \
                --disable-rpath \
                --disable-pulseaudio"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)}"
PACKAGECONFIG[alsa] = "--enable-alsa,--disable-alsa,alsa-lib,"

PARALLEL_MAKE = ""

EXTRA_AUTORECONF += "--include=acinclude --exclude=autoheader"

do_configure_prepend() {
        # Remove old libtool macros.
        MACROS="libtool.m4 lt~obsolete.m4 ltoptions.m4 ltsugar.m4 ltversion.m4"
        for i in ${MACROS}; do
               rm -f acinclude/$i
        done
        export SYSROOT=$PKG_CONFIG_SYSROOT_DIR
}

BBCLASSEXTEND = "nativesdk"
