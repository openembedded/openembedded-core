PR = "r9"

export IMAGE_BASENAME = "oh-image-sdk"

XSERVER ?= "xserver-kdrive-fbdev"

DEPENDS = "\
    task-oh \
    task-oh-sdk"
    
RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base \
    task-oh-standard \
    task-oh-devtools \
    task-oh-testapps \
    task-oh-sdk \
    ${XSERVER} "

export IPKG_INSTALL = "${RDEPENDS}"

inherit image_ipk
LICENSE = MIT
