LICENSE = "GPL"
DEPENDS = "zlib"
PR = "r7"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    http://fabrice.bellard.free.fr/qemu/qemu-0.9.1.tar.gz \
    file://02_snapshot_use_tmpdir.patch;patch=1;pnum=0 \
    file://04_do_not_print_rtc_freq_if_ok.patch;patch=1;pnum=1 \
    file://05_non-fatal_if_linux_hd_missing.patch;patch=1;pnum=1 \
    file://06_exit_segfault.patch;patch=1;pnum=0 \
    file://10_signal_jobs.patch;patch=1;pnum=0 \
    file://11_signal_sigaction.patch;patch=1;pnum=0 \
    file://22_net_tuntap_stall.patch;patch=1;pnum=0 \
    file://31_syscalls.patch;patch=1;pnum=0 \
    file://32_syscall_sysctl.patch;patch=1;pnum=0 \
    file://33_syscall_ppc_clone.patch;patch=1;pnum=0 \
    file://39_syscall_fadvise64.patch;patch=1;pnum=0 \
    file://41_arm_fpa_sigfpe.patch;patch=1;pnum=0 \
    file://52_ne2000_return.patch;patch=1;pnum=1 \
    file://61_safe_64bit_int.patch;patch=1;pnum=0 \
    file://63_sparc_build.patch;patch=1;pnum=0 \
    file://64_ppc_asm_constraints.patch;patch=1;pnum=1 \
    file://65_kfreebsd.patch;patch=1;pnum=0 \
    file://66_tls_ld.patch;patch=1;pnum=0 \
    file://91-oh-sdl-cursor.patch;patch=1;pnum=0 \
    file://qemu-0.9.0-nptl.patch;patch=1 \
    file://qemu-0.9.0-nptl-update.patch;patch=1 \
    file://qemu-amd64-32b-mapping-0.9.0.patch;patch=1 \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://fix_segfault.patch;patch=1 \
    file://writev_fix.patch;patch=1 \
    file://configure_symlinkpath_fix.patch;patch=1 \
    file://disable-error-in-configure.patch;patch=1 \
    file://no-strip.patch;patch=1"

S = "${WORKDIR}/qemu-${PV}"

#EXTRA_OECONF += "--disable-sdl"
#EXTRA_OECONF += "--target-list=arm-linux-user,arm-softmmu"
EXTRA_OECONF += "--disable-gfx-check"

inherit autotools

do_configure() {
    oe_runconf
}
