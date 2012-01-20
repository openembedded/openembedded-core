# Simple initramfs image. Mostly used for live images.
DESCRIPTION = "A core-image-minimal image that has the Minimal RAM-based \
Initial Root Filesystem (initramfs) as part of the kernel, which allows the \
system to find the first 'init' program more efficiently."

IMAGE_INSTALL = "initramfs-live-boot initramfs-live-install busybox udev base-passwd"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

export IMAGE_BASENAME = "core-image-minimal-initramfs"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
