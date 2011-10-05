require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PR = "r0"

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
    "

SRC_URI_append_virtclass-native = "\
    ${QEMUGLPATCHES} \
    "

SRC_URI_append_virtclass-nativesdk = "\
    ${QEMUGLPATCHES} \
    file://glflags.patch \
    "
SRC_URI[md5sum] = "dbc55b014bcd21b98e347f6a90f7fb6d"
SRC_URI[sha256sum] = "0197e52dba07aeb6dfe0343b0c2ae08ed374d2cb0af3bb9ec73fed5baa0cb74d"

S = "${WORKDIR}/qemu-${PV}"
