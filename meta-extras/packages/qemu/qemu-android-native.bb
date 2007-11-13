LICENSE = "GPL"
DEPENDS = "zlib-native"
PV = "0.0-rc20"
PR = "r0"

SRC_URI = "http://android.googlecode.com/files/android-emulator-m3-rc20.tar.bz2 \
           file://fixes.patch;patch=1"

S = "${WORKDIR}/android-emulator-20071111/qemu"

EXTRA_OECONF = "--disable-gfx-check --target-list=arm-softmmu --enable-nand --enable-skins --enable-trace --enable-shaper --use-sdl-config=${prefix}/hackedsdl/bin/sdl-config --disable-user --disable-kqemu --enable-sdl --static-png --static-sdl"

inherit autotools
inherit native

do_unfubar () {
    chmod -R u+w ${WORKDIR}/
    cd ${WORKDIR}/android-emulator-20071111/sdl
    configure --prefix=${prefix}/hackedsdl
    make
    make install
}

addtask unfubar after do_unpack before do_patch

