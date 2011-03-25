DESCRIPTION = "open source processor emulator"
HOMEPAGE = "http://qemu.org"
LICENSE = "GPLv2 & LGPLv2.1"
DEPENDS = "zlib alsa-lib"

# QEMU_TARGETS is overridable variable
QEMU_TARGETS ?= "arm i386 mips mipsel mips64 mips64el ppc sh4 x86_64"

require qemu-targets.inc

EXTRA_OECONF += "--target-list=${@get_qemu_target_list(d)} --disable-werror --disable-vnc-tls --enable-kvm --audio-drv-list=oss,alsa --audio-card-list=ac97,es1370"

#EXTRA_OECOF += "--disable-sdl"

inherit autotools

# For our gl powered QEMU you need libGL and SDL headers
do_configure_prepend_virtclass-native() {
    libgl='no'
    libsdl='no'

    test -e /usr/lib/libGL.so -a -e /usr/lib/libGLU.so && libgl='yes'
    test -e /usr/lib64/libGL.so -a -e /usr/lib64/libGLU.so && libgl='yes'

    test -e /usr/lib/pkgconfig/sdl.pc -o -e /usr/lib64/pkgconfig/sdl.pc -o -e /usr/include/SDL/SDL.h && libsdl='yes'


    if [ "$libsdl" != 'yes' -o "$libgl" != 'yes' ]; then
       echo "You need libGL.so and libGLU.so to exist in your library path and the development headers for SDL installed to build qemu-native.
       Ubuntu package names are: libgl1-mesa-dev, libglu1-mesa-dev and libsdl1.2-dev"
       exit 1;
    fi
}

do_configure() {
    ${S}/configure --prefix=${prefix} ${EXTRA_OECONF}
    chmod a+x ${S}/target-i386/beginend_funcs.sh
}

SRC_URI_append_virtclass-nativesdk = " file://glflags.patch;patch=1"
DEPENDS_virtclass-nativesdk = "zlib-nativesdk libsdl-nativesdk qemugl-nativesdk"
RDEPENDS_virtclass-nativesdk = "libsdl-nativesdk"
EXTRA_OECONF_virtclass-nativesdk += "--target-list=${@get_qemu_target_list(d)} --disable-werror --disable-vnc-tls --cross-prefix=${TARGET_PREFIX}"

BBCLASSEXTEND = "native nativesdk"
