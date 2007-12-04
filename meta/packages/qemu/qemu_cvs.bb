LICENSE = "GPL"
DEPENDS = "zlib"
PV = "0.9.0+cvs${SRCDATE}"
PR = "r4"

DEFAULT_PREFERENCE = "-1"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
    file://02_snapshot_use_tmpdir.patch;patch=1;pnum=0 \
    file://03_machines_list_no_error.patch;patch=1;pnum=0 \
    file://04_do_not_print_rtc_freq_if_ok.patch;patch=1;pnum=1 \
    file://05_non-fatal_if_linux_hd_missing.patch;patch=1;pnum=1 \
    file://06_exit_segfault.patch;patch=1;pnum=0 \
    file://10_signal_jobs.patch;patch=1;pnum=0 \
    file://11_signal_sigaction.patch;patch=1;pnum=0 \
    file://12_signal_powerpc_support.patch;patch=1;pnum=1 \
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
    file://93-oh-pl110-rgb.patch;patch=1;pnum=0 \
    file://qemu-0.9.0-nptl.patch;patch=1 \
    file://qemu-0.9.0-nptl-update.patch;patch=1 \
    file://qemu-amd64-32b-mapping-0.9.0.patch;patch=1 \
    file://workaround_bad_futex_headers.patch;patch=1 \
    file://fix_segfault.patch;patch=1"

#    svn://svn.o-hand.com/repos/misc/trunk/qemu-packaging/qemu;module=debian;proto=http;srcdate=20070119 \
#    file://debian/patches/21_net_soopts.patch;patch=1;pnum=0 \
#    file://debian/patches/35_syscall_sockaddr.patch;patch=1;pnum=0 \
#    file://debian/patches/43_arm_cpustate.patch;patch=1;pnum=0 \
#    file://debian/patches/62_linux_boot_nasm.patch;patch=1;pnum=0 \
#    file://debian/patches/67_ppc_ftbfs.patch;patch=1;pnum=0 \
#    file://debian/patches/80_ui_curses.patch;patch=1;pnum=0 \
#    file://debian/patches/96-x.patch;patch=1"

S = "${WORKDIR}/qemu"

#EXTRA_OECONF += "--disable-sdl"
#EXTRA_OECONF += "--target-list=arm-linux-user"
EXTRA_OECONF += "--disable-gfx-check"

inherit autotools

