LICENSE = "GPL"
DEPENDS = "zlib"
PV = "0.10.2+git${SRCREV}"
PR = "r5"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}/:${FILE_DIRNAME}/qemu-git/"

SRC_URI = "\
    git://git.sv.gnu.org/qemu.git;protocol=git \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://qemu-git-qemugl-host.patch;patch=1 \
    file://no-strip.patch;patch=1 \
    file://fix-dirent.patch;patch=1 \
    file://fix-nogl.patch;patch=1 \
    file://zlibsearch.patch;patch=1 \
    file://qemugl-allow-glxcontext-release.patch;patch=1 "

S = "${WORKDIR}/git"

#EXTRA_OECONF += "--disable-sdl"
EXTRA_OECONF += "--target-list=arm-linux-user,arm-softmmu,i386-softmmu,x86_64-softmmu"
EXTRA_OECONF += "--disable-gfx-check"

inherit autotools

do_configure() {
    ${S}/configure --prefix=${prefix} ${EXTRA_OECONF}
    chmod a+x ${S}/target-i386/beginend_funcs.sh
}
