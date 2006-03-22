PR = "r9"

export IMAGE_BASENAME = "oh-devel"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "\
    task-bootstrap \
    task-oh"
    
RDEPENDS = "\
    task-bootstrap \
    task-oh-base \
    task-oh-devel \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"
#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
