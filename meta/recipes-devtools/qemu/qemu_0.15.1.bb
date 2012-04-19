require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PR = "r6"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    http://wiki.qemu.org/download/qemu-${PV}.tar.gz \
    file://powerpc_rom.bin \
    file://no-strip.patch \
    file://linker-flags.patch \
    file://qemu-vmware-vga-depth.patch \
    file://fix-configure-checks.patch \
    file://fallback-to-safe-mmap_min_addr.patch \
    file://larger_default_ram_size.patch \
    file://arm-bgr.patch \
    file://a4d1f142542935b90d2eb30f3aead4edcf455fe6.patch \
    "

# Only use the GL passthrough patches for native/nativesdk versions
QEMUGLPATCHES = "\
    file://qemu-git-qemugl-host.patch \
    file://fix-nogl.patch \
    file://qemugl-allow-glxcontext-release.patch \
    file://init-info.patch \
    file://enable-i386-linux-user.patch \
    file://qemugl-fix.patch \
    file://opengl-sdl-fix.patch \
    file://opengl-args-copy-fix.patch \
    "

SRC_URI_append_virtclass-native = "\
    ${QEMUGLPATCHES} \
    "

SRC_URI_append_virtclass-nativesdk = "\
    ${@base_contains('DISTRO_FEATURES', 'x11', '${QEMUGLPATCHES} file://glflags.patch', '', d)}  \
    "
SRC_URI[md5sum] = "34f17737baaf1b3495c89cd6d4a607ed"
SRC_URI[sha256sum] = "7705b14d9b8e4df4a0b1790980e618084261e8daef0672a1aa7a830a0f3db5ba"

S = "${WORKDIR}/qemu-${PV}"

do_configure_prepend_virtclass-nativesdk() {
	if [ "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}" = "" ] ; then
		# Undo the -lX11 added by linker-flags.patch
		sed -i 's/-lX11//g' Makefile.target
	fi
}

