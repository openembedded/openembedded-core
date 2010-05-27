LICENSE = "GPL"
DEPENDS = "zlib"
PR = "r9"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    http://download.savannah.gnu.org/releases/qemu/qemu-0.12.0.tar.gz \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://qemu-git-qemugl-host.patch;patch=1 \
    file://no-strip.patch;patch=1 \
    file://fix-dirent.patch;patch=1 \
    file://fix-nogl.patch;patch=1 \
    file://qemugl-allow-glxcontext-release.patch;patch=1"

S = "${WORKDIR}/qemu-${PV}"

EXTRA_OECONF = "--target-list=arm-linux-user,arm-softmmu,i386-softmmu,x86_64-softmmu,mips-linux-user,mips-softmmu --disable-werror --disable-vnc-tls"
#EXTRA_OECONF += "--disable-sdl"

inherit autotools

# For our gl powered QEMU you need libGL and SDL headers
do_configure_prepend_virtclass-native() {
    libgl='no'
    libsdl='no'

    test -e /usr/lib/libGL.so -a -e /usr/lib/libGLU.so && libgl='yes'
    test -e /usr/lib64/libGL.so -a -e /usr/lib64/libGLU.so && libgl='yes'

    test -e /usr/lib/pkgconfig/sdl.pc -o -e /usr/lib64/pkgconfig/sdl.pc && libsdl='yes'

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
EXTRA_OECONF_virtclass-nativesdk = "--target-list=arm-linux-user,arm-softmmu,i386-softmmu,x86_64-softmmu,mips-linux-user,mips-softmmu --disable-vnc-tls --cc=${HOST_PREFIX}gcc"

BBCLASSEXTEND = "native nativesdk"
