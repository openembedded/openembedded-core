SUMMARY = "Basic initramfs to boot a fully-featured rootfs"
DESCRIPTION = "Small initramfs that contains just udev and init, to find the real rootfs."
LICENSE = "MIT"

INITRAMFS_SCRIPTS ?= "initramfs-framework-base initramfs-module-udev initramfs-module-rootfs"

inherit image

PACKAGE_INSTALL = " \
    ${VIRTUAL-RUNTIME_base-utils} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd-initramfs', ' \
        base-files \
        coreutils \
        e2fsprogs-mke2fs \
        os-release-initrd \
        util-linux-blkid \
        util-linux-mount \
        util-linux-umount \
        ${VIRTUAL-RUNTIME_init_manager} \
        ${VIRTUAL-RUNTIME_dev_manager} \
    ', '${INITRAMFS_SCRIPTS}', d)} \
    base-passwd \
    busybox-udhcpc \
    kernel-initrd-modules \
    libkmod \
"

# reduce size
NO_RECOMMENDATIONS = "1"

# don't install automatically, pick manually instead
MACHINE_EXTRA_RDEPENDS = ""
MACHINE_EXTRA_RRECOMMENDS = ""

# Ensure the initramfs only contains the bare minimum
IMAGE_FEATURES = ""
IMAGE_LINGUAS = ""

# Don't allow the initramfs to contain a kernel, as kernel modules will depend
# on the kernel image.
PACKAGE_EXCLUDE = "kernel-image-*"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_NAME_SUFFIX ?= ""
IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
