PR = "r10"

export IMAGE_BASENAME = "oh-image-pda"

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

inherit image_ipk
LICENSE = MIT
