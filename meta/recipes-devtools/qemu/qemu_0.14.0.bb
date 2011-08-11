require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PR = "r2"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    http://download.savannah.gnu.org/releases/qemu/qemu-${PV}.tar.gz \
    file://powerpc_rom.bin \
    file://no-strip.patch \
    file://linker-flags.patch \
    file://qemu-vmware-vga-depth.patch \
    file://fix-configure-checks.patch \
    file://fallback-to-safe-mmap_min_addr.patch \
    file://spice-qxl-locking-fix-for-qemu-kvm.patch \
    file://Detect-and-use-GCC-atomic-builtins-for-locking.patch \
    file://larger_default_ram_size.patch \
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

SRC_URI[md5sum] = "f9d145d5c09de9f0984ffe9bd1229970"
SRC_URI[sha256sum] = "ba21e84d7853217830e167dae9999cdbff481189c6a0bb600ac7fb7201453108"

S = "${WORKDIR}/qemu-${PV}"
