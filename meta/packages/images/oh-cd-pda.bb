DESCRIPTION = "Bootable CD"
DEPENDS = "oh-image-pda"

COMPATIBLE_MACHINE = "bootcdx86"

IMAGE_NAME = "${PN}-${DATETIME}"
AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/oh-image-pda-${MACHINE}.ext2"
LABELS = "poky"
APPEND = "root=/dev/ram0"

inherit bootimg
