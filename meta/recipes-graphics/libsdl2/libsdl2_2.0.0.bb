SUMMARY = "Simple DirectMedia Layer"
DESCRIPTION = "Simple DirectMedia Layer is a cross-platform multimedia \
library designed to provide low level access to audio, keyboard, mouse, \
joystick, 3D hardware via OpenGL, and 2D video framebuffer."
HOMEPAGE = "http://www.libsdl.org"
BUGTRACKER = "http://bugzilla.libsdl.org/"

SECTION = "libs"

LICENSE = "Zlib"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=0605ca7e995ab1217e0bb988731a87fe"

PROVIDES = "virtual/libsdl2"

DEPENDS = "${@base_contains('DISTRO_FEATURES', 'directfb', 'directfb', '', d)} \
           ${@base_contains('DISTRO_FEATURES', 'opengl', 'virtual/libgl', '', d)} \
           ${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/libx11 libxext libxrandr libxrender', '', d)} \
           tslib"
DEPENDS_class-nativesdk = "${@base_contains('DISTRO_FEATURES', 'x11', 'virtual/nativesdk-libx11 nativesdk-libxrandr nativesdk-libxrender nativesdk-libxext', '', d)}"

SRC_URI = "http://www.libsdl.org/release/SDL2-${PV}.tar.gz \
       "

S = "${WORKDIR}/SDL2-${PV}"

SRC_URI[md5sum] = "beec89afb6edcc6f0abc4114f2e6bcf7"
SRC_URI[sha256sum] = "6f6ac8153d90e06a118474d5400624ae82b3d3e080915505db27cebc52b27178"

inherit autotools lib_package binconfig pkgconfig

EXTRA_OECONF = "--disable-oss --disable-esd --disable-arts \
                --disable-diskaudio --disable-nas --disable-esd-shared --disable-esdtest \
                --disable-video-dummy \
                --enable-input-tslib --enable-pthreads \
                ${@base_contains('DISTRO_FEATURES', 'directfb', '--enable-video-directfb', '--disable-video-directfb', d)} \
                ${@base_contains('DISTRO_FEATURES', 'opengl', '--enable-video-opengl', '--disable-video-opengl', d)} \
                ${@base_contains('DISTRO_FEATURES', 'x11', '--enable-video-x11', '--disable-video-x11', d)} \
                --enable-sdl-dlopen \
                --disable-rpath \
                --disable-pulseaudio"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)}"
PACKAGECONFIG[alsa] = "--enable-alsa --disable-alsatest,--disable-alsa,alsa-lib,"

PARALLEL_MAKE = ""

EXTRA_AUTORECONF += "--include=acinclude --exclude=autoheader"

do_configure_prepend() {
        # Remove old libtool macros.
        MACROS="libtool.m4 lt~obsolete.m4 ltoptions.m4 ltsugar.m4 ltversion.m4"
        for i in ${MACROS}; do
               rm -f ${S}/acinclude/$i
        done
        export SYSROOT=$PKG_CONFIG_SYSROOT_DIR
}
