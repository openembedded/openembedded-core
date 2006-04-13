PR = "r0"

export IMAGE_BASENAME = "oh-image-minimal"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "task-oh"
    
RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-min-extras \
    task-oh-base \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"
#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
