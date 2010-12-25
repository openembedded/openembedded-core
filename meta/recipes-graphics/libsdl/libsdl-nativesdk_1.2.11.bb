DESCRIPTION = "Simple DirectMedia Layer - native Edition"
HOMEPAGE = "http://www.libsdl.org"
SECTION = "libs"
LICENSE = "LGPLv2.1"
DEPENDS = "libx11-nativesdk libxext-nativesdk libxrandr-nativesdk libxrender-nativesdk"
RDEPENDS = "libx11-nativesdk libxrandr-nativesdk libxrender-nativesdk libxext-nativesdk"
PR = "r4"

LIC_FILES_CHKSUM = "file://COPYING;md5=27818cd7fd83877a8e3ef82b82798ef4"

SRC_URI = "http://www.libsdl.org/release/SDL-${PV}.tar.gz \
	   file://acinclude.m4 \
	   file://configure_tweak.patch;patch=1 \
	   file://kernel-asm-page.patch;patch=1 "
S = "${WORKDIR}/SDL-${PV}"

inherit autotools binconfig pkgconfig nativesdk

EXTRA_OECONF = "--disable-static --disable-debug --disable-cdrom --enable-threads --enable-timers --enable-endian \
                --enable-file --disable-oss --disable-alsa --disable-esd --disable-arts \
                --disable-diskaudio --disable-nas --disable-esd-shared --disable-esdtest \
                --disable-mintaudio --disable-nasm --enable-video-x11 --disable-video-dga \
                --disable-video-fbcon --disable-video-directfb --disable-video-ps2gs \
                --disable-video-xbios --disable-video-gem --disable-video-dummy \
                --disable-video-opengl --enable-input-events --enable-pthreads \
		--disable-video-svga \
                --disable-video-picogui --disable-video-qtopia --enable-dlopen"

PARALLEL_MAKE = ""

do_configure_prepend() {
	cp ${WORKDIR}/acinclude.m4 ${S}/acinclude.m4
}
