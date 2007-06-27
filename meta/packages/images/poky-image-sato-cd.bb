DESCRIPTION = "Bootable CD"
DEPENDS = "poky-image-sato"

COMPATIBLE_MACHINE = "bootcdx86"

IMAGE_NAME = "${PN}-${DATETIME}"
AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/poky-image-sato-${MACHINE}.ext2"
LABELS = "poky"
APPEND = "root=/dev/ram0"

inherit bootimg
