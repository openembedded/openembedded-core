SUMMARY = "Modular initramfs system"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_base-utils}"

PR = "r2"

# While the packages maybe an allarch due to it being a
# simple script, reality is that it is Host specific based
# on the COMPATIBLE_HOST below, which needs to take precedence
#inherit allarch
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "file://init \
           file://rootfs \
           file://finish \
           file://mdev \
           file://udev \
           file://e2fs \
           file://debug \
           file://setup-live \
           file://install-efi.sh"

S = "${WORKDIR}"

do_install() {
    install -d ${D}/init.d

    # base
    install -m 0755 ${WORKDIR}/init ${D}/init
    install -m 0755 ${WORKDIR}/rootfs ${D}/init.d/90-rootfs
    install -m 0755 ${WORKDIR}/finish ${D}/init.d/99-finish

    # setup-live
    install -m 0755 ${WORKDIR}/setup-live ${D}/init.d/80-setup-live

    # mdev
    install -m 0755 ${WORKDIR}/mdev ${D}/init.d/01-mdev

    # udev
    install -m 0755 ${WORKDIR}/udev ${D}/init.d/01-udev

    # e2fs
    install -m 0755 ${WORKDIR}/e2fs ${D}/init.d/10-e2fs

    # debug
    install -m 0755 ${WORKDIR}/debug ${D}/init.d/00-debug

    # install-efi
    install -m 0755 ${WORKDIR}/install-efi.sh ${D}/init.d/install-efi.sh

    # Create device nodes expected by some kernels in initramfs
    # before even executing /init.
    install -d ${D}/dev
    mknod -m 622 ${D}/dev/console c 5 1
}

PACKAGES = "${PN}-base \
            initramfs-module-mdev \
            initramfs-module-udev \
            initramfs-module-e2fs \
            initramfs-module-rootfs \
            initramfs-module-debug \
            initramfs-module-setup-live \
            initramfs-module-install-efi"

FILES_${PN}-base = "/init /init.d/99-finish /dev"

# 99-finish in base depends on some other module which mounts
# the rootfs, like 90-rootfs. To replace that default, use
# BAD_RECOMMENDATIONS += "initramfs-module-rootfs" in your
# initramfs recipe and install something else, or install
# something that runs earlier (for example, a 89-my-rootfs)
# and mounts the rootfs. Then 90-rootfs will proceed immediately.
RRECOMMENDS_${PN}-base += "initramfs-module-rootfs"

SUMMARY_initramfs-module-mdev = "initramfs support for mdev"
RDEPENDS_initramfs-module-mdev = "${PN}-base busybox-mdev"
FILES_initramfs-module-mdev = "/init.d/01-mdev"

SUMMARY_initramfs-module-udev = "initramfs support for udev"
RDEPENDS_initramfs-module-udev = "${PN}-base udev"
FILES_initramfs-module-udev = "/init.d/01-udev"

SUMMARY_initramfs-module-setup-live = "initramfs support for setup live"
RDEPENDS_initramfs-module-setup-live = "${PN}-base udev-extraconf"
FILES_initramfs-module-setup-live = "/init.d/80-setup-live"

SUMMARY_initramfs-module-e2fs = "initramfs support for ext4/ext3/ext2 filesystems"
RDEPENDS_initramfs-module-e2fs = "${PN}-base"
FILES_initramfs-module-e2fs = "/init.d/10-e2fs"

SUMMARY_initramfs-module-rootfs = "initramfs support for locating and mounting the root partition"
RDEPENDS_initramfs-module-rootfs = "${PN}-base"
FILES_initramfs-module-rootfs = "/init.d/90-rootfs"

SUMMARY_initramfs-module-debug = "initramfs dynamic debug support"
RDEPENDS_initramfs-module-debug = "${PN}-base"
FILES_initramfs-module-debug = "/init.d/00-debug"

SUMMARY_initramfs-module-install-efi = "initramfs support for installation option"
RDEPENDS_initramfs-module-install-efi = "${PN}-base parted e2fsprogs-mke2fs dosfstools util-linux-blkid"
FILES_initramfs-module-install-efi = "/init.d/install-efi.sh"
