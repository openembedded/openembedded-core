# Sample initramfs image, very minimal

IMAGE_INSTALL = "initramfs-live-boot busybox udev"

export IMAGE_BASENAME = "poky-image-minimal-initramfs"
IMAGE_LINGUAS = ""

inherit poky-image
