# Simple initramfs image. Mostly used for live images.

IMAGE_INSTALL = "initramfs-live-boot initramfs-live-install busybox udev base-passwd"

export IMAGE_BASENAME = "poky-image-minimal-initramfs"
IMAGE_LINGUAS = ""

inherit poky-image
