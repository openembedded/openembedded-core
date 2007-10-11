DESCRIPTION = "Bootable CD"

COMPATIBLE_MACHINE = "bootcdx86"

IMAGE_NAME = "${PN}-${DATETIME}"
AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/poky-image-sato-${MACHINE}.ext2"
LABELS = "poky"
APPEND = "root=/dev/ram0"

do_bootimg[depends] += "poky-image-sato:do_rootfs"

inherit bootimg
