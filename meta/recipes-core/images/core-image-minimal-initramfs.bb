# Simple initramfs image. Mostly used for live images.

IMAGE_INSTALL = "initramfs-live-boot initramfs-live-install busybox udev base-passwd"

export IMAGE_BASENAME = "core-image-minimal-initramfs"
IMAGE_LINGUAS = ""

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "8192"
