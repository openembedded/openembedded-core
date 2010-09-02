require qemu.inc

PV = "0.12.4"
PR = "r11"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}/:${FILE_DIRNAME}/qemu-git/"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    git://git.sv.gnu.org/qemu.git;protocol=git \
    file://workaround_bad_futex_headers.patch \
    file://qemu-git-qemugl-host.patch \
    file://no-strip.patch \
    file://fix-dirent.patch \
    file://fix-nogl.patch \
    file://qemugl-allow-glxcontext-release.patch \
    file://linker-flags.patch \
    file://qemu-vmware-vga-depth.patch \
    file://enable-i386-linux-user.patch"

S = "${WORKDIR}/git"

