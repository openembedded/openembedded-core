require qemu.inc

SRCREV = "56a60dd6d619877e9957ba06b92d2f276e3c229d"

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

PV = "0.14.0"
PR = "r2"

FILESPATH = "${FILE_DIRNAME}/qemu-${PV}/:${FILE_DIRNAME}/qemu-git/"
FILESDIR = "${WORKDIR}"

SRC_URI = "\
    git://git.qemu.org/qemu.git;protocol=git \
    file://qemu-git-qemugl-host.patch \
    file://no-strip.patch \
    file://fix-nogl.patch \
    file://qemugl-allow-glxcontext-release.patch \
    file://linker-flags.patch \
    file://qemu-vmware-vga-depth.patch \
    file://enable-i386-linux-user.patch"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"


