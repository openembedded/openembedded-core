LICENSE = "GPL"
DEPENDS = "zlib"
PV = "0.10.2+git${SRCREV}"
PR = "r1"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}/:${FILE_DIRNAME}/qemu-git/"

SRC_URI = "\
    git://git.sv.gnu.org/qemu.git;protocol=git \
    file://06_exit_segfault.patch;patch=1;pnum=0 \
    file://11_signal_sigaction.patch;patch=1;pnum=0 \
    file://22_net_tuntap_stall.patch;patch=1;pnum=0 \
    file://31_syscalls.patch;patch=1;pnum=0 \
    file://52_ne2000_return.patch;patch=1;pnum=1 \
    file://63_sparc_build.patch;patch=1;pnum=0 \
    file://66_tls_ld.patch;patch=1;pnum=0 \
    file://91-oh-sdl-cursor.patch;patch=1;pnum=0 \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://qemu-add-gl-host-code.patch;patch=1 \
    file://no-strip.patch;patch=1 \
    file://fix-dirent.patch;patch=1 \
    file://zlibsearch.patch;patch=1 "

#    file://qemu-amd64-32b-mapping-0.9.0.patch;patch=1 

S = "${WORKDIR}/git"

#EXTRA_OECONF += "--disable-sdl"
EXTRA_OECONF += "--target-list=arm-linux-user,arm-softmmu,i386-softmmu,x86_64-softmmu"
EXTRA_OECONF += "--disable-gfx-check"

inherit autotools

do_configure() {
    ${S}/configure --prefix=${prefix} ${EXTRA_OECONF}
    chmod a+x ${S}/target-i386/beginend_funcs.sh
}
