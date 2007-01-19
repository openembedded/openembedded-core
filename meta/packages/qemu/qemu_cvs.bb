LICENSE = "GPL"
DEPENDS = "zlib"
PV = "0.8.2+cvs${SRCDATE}"
PR = "r4"

FILESDIR = "${WORKDIR}"

SRC_URI = "\
    cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
    svn://svn.o-hand.com/repos/misc/trunk/qemu-packaging/qemu;module=debian;proto=http;srcdate=20070119 \
    file://debian/patches/01_doc_typos.patch;patch=1;pnum=1 \
    file://debian/patches/02_snapshot_use_tmpdir.patch;patch=1;pnum=0 \
    file://debian/patches/03_machines_list_no_error.patch;patch=1;pnum=0 \
    file://debian/patches/04_do_not_print_rtc_freq_if_ok.patch;patch=1;pnum=1 \
    file://debian/patches/05_non-fatal_if_linux_hd_missing.patch;patch=1;pnum=1 \
    file://debian/patches/06_exit_segfault.patch;patch=1;pnum=0 \
    file://debian/patches/10_signal_jobs.patch;patch=1;pnum=0 \
    file://debian/patches/11_signal_sigaction.patch;patch=1;pnum=0 \
    file://debian/patches/12_signal_powerpc_support.patch;patch=1;pnum=1 \
    file://debian/patches/21_net_soopts.patch;patch=1;pnum=0 \
    file://debian/patches/22_net_tuntap_stall.patch;patch=1;pnum=0 \
    file://debian/patches/30_syscall_ipc.patch;patch=1;pnum=0 \
    file://debian/patches/31_syscalls.patch;patch=1;pnum=0 \
    file://debian/patches/32_syscall_sysctl.patch;patch=1;pnum=0 \
    file://debian/patches/33_syscall_ppc_clone.patch;patch=1;pnum=0 \
    file://debian/patches/35_syscall_sockaddr.patch;patch=1;pnum=0 \
    file://debian/patches/36_syscall_prctl.patch;patch=1;pnum=0 \
    file://debian/patches/37_syscall_mount.patch;patch=1;pnum=0 \
    file://debian/patches/38_syscall_arm_statfs64.patch;patch=1;pnum=0 \
    file://debian/patches/39_syscall_fadvise64.patch;patch=1;pnum=0 \
    file://debian/patches/41_arm_fpa_sigfpe.patch;patch=1;pnum=0 \
    file://debian/patches/43_arm_cpustate.patch;patch=1;pnum=0 \
    file://debian/patches/51_serial_small_divider.patch;patch=1;pnum=1 \
    file://debian/patches/52_ne2000_return.patch;patch=1;pnum=1 \
    file://debian/patches/55_unmux_socketcall.patch;patch=1;pnum=0 \
    file://debian/patches/60_ppc_ld.patch;patch=1;pnum=1 \
    file://debian/patches/61_safe_64bit_int.patch;patch=1;pnum=0 \
    file://debian/patches/62_linux_boot_nasm.patch;patch=1;pnum=0 \
    file://debian/patches/63_sparc_build.patch;patch=1;pnum=0 \
    file://debian/patches/64_ppc_asm_constraints.patch;patch=1;pnum=1 \
    file://debian/patches/65_kfreebsd.patch;patch=1;pnum=0 \
    file://debian/patches/66_tls_ld.patch;patch=1;pnum=0 \
    file://debian/patches/67_ppc_ftbfs.patch;patch=1;pnum=0 \
    file://debian/patches/90-oh-wacom.patch;patch=1;pnum=0 \
    file://debian/patches/91-oh-sdl-cursor.patch;patch=1;pnum=0 \
    file://debian/patches/92-oh-pci-irq-sharing.patch;patch=1;pnum=0 \
    file://debian/patches/93-oh-pl110-rgb.patch;patch=1;pnum=0 \
    file://debian/patches/94-oh-arm-nptl.patch;patch=1;pnum=1 \
    file://debian/patches/95-oh-compiler.patch;patch=1 \
    file://debian/patches/96-x.patch;patch=1"
#    file://debian/patches/80_ui_curses.patch;patch=1;pnum=0 \

#           file://configure.patch;patch=1;pnum=1    \
#           file://qemu-sdl-cursor.patch;patch=1;pnum=1    \
#	   file://arm_nptl.patch;patch=1;pnum=1     \
#	   file://pl110_rgb-r0.patch;patch=1;pnum=1 \
#	   file://qemu-pci-irq-sharing.patch;patch=1;pnum=1 \
#	   file://compiler.patch;patch=1;pnum=1 \
#	   file://qemu-usb-wacom-0.8.2.patch;patch=1;pnum=1 \
#           file://qemu-usb-wacom-pressure.patch;patch=1;pnum=1 \
#	   file://qemu-usb-wacom-buttons.patch;patch=1"

S = "${WORKDIR}/qemu"

#EXTRA_OECONF = "--disable-sdl"

inherit autotools

