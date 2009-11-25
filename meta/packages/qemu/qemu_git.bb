LICENSE = "GPL"
DEPENDS = "zlib"
PV = "0.10.6+git${SRCREV}"
PR = "r0"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}/:${FILE_DIRNAME}/qemu-git/"

SRC_URI = "\
    git://git.sv.gnu.org/qemu.git;protocol=git \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://qemu-git-qemugl-host.patch;patch=1 \
    file://no-strip.patch;patch=1 \
    file://fix-dirent.patch;patch=1 \
    file://fix-nogl.patch;patch=1 \
    file://zlibsearch.patch;patch=1 \
    file://qemugl-allow-glxcontext-release.patch;patch=1 \
    file://2ca2078e287174522e3a6229618947d3d285b8c0.patch;patch=1"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--target-list=arm-linux-user,arm-softmmu,i386-softmmu,x86_64-softmmu --disable-gfx-check"
#EXTRA_OECONF += "--disable-sdl"

inherit autotools

do_configure() {
    ${S}/configure --prefix=${prefix} ${EXTRA_OECONF}
    chmod a+x ${S}/target-i386/beginend_funcs.sh
}

SRC_URI_append_virtclass-nativesdk = " file://glflags.patch;patch=1"
DEPENDS_virtclass-nativesdk = "zlib-nativesdk libsdl-nativesdk qemugl-nativesdk"
RDEPENDS_virtclass-nativesdk = "libsdl-nativesdk"
EXTRA_OECONF_virtclass-nativesdk = "--target-list=arm-linux-user,arm-softmmu,i386-softmmu --disable-vnc-tls --cc=${HOST_PREFIX}gcc"

BBCLASSEXTEND = "native nativesdk"
