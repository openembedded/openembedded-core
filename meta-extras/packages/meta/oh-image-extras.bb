PR = "r9"

export IMAGE_BASENAME = "oh-extras"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "\
    task-oh \
    task-oh-extras"
    
RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base \
    task-oh-standard \
    task-oh-testapps \
    task-oh-devtools \
    task-oh-extraapps \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"
#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
