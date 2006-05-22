PR = "r9"

export IMAGE_BASENAME = "oh-devel"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "\
    task-bootstrap \
    task-oh \
    task-oh-sdk"
    
RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base \
    task-oh-standard \
    task-oh-devel \
    task-oh-sdk \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"
#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
