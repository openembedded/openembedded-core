require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI = "\
    http://wiki.qemu.org/download/qemu-${PV}.tar.bz2 \
    file://powerpc_rom.bin \
    file://no-strip.patch \
    file://linker-flags.patch \
    file://qemu-vmware-vga-depth.patch \
    file://fix-configure-checks.patch \
    file://fallback-to-safe-mmap_min_addr.patch \
    file://larger_default_ram_size.patch \
    file://arm-bgr.patch \
    "
SRC_URI[md5sum] = "78eb1e984f4532aa9f2bdd3c127b5b61"
SRC_URI[sha256sum] = "c8b84420d9f4869397f84cad2dabd9a475b7723d619a924a873740353e9df936"

PR = "r2"

SRC_URI_append_virtclass-nativesdk = "\
    file://relocatable_sdk.patch \
    "

do_configure_prepend_virtclass-nativesdk() {
	if [ "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}" = "" ] ; then
		# Undo the -lX11 added by linker-flags.patch
		sed -i 's/-lX11//g' Makefile.target
	fi
}
do_configure_prepend_virtclass-native() {
	# Undo the -lX11 added by linker-flags.patch, don't assume that host has libX11 installed
	sed -i 's/-lX11//g' Makefile.target
}

