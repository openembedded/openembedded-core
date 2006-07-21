PR = "r10"

export IMAGE_BASENAME = "oh-image"

GUI_MACHINE_CLASS ?= "none"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "task-oh"

RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base \
    task-oh-standard \
    ${@base_conditional("DISTRO_TYPE", "debug", "task-oh-devtools", "",d)} \
    ${@base_conditional("DISTRO_TYPE", "debug", "task-oh-testapps", "",d)} \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"
#ROOTFS_POSTPROCESS_COMMAND += "zap_root_password; "

inherit image_ipk
LICENSE = MIT
