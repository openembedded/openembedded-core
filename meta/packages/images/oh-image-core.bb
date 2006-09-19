PR = "r0"

export IMAGE_BASENAME = "oh-image-core"

DEPENDS = "task-oh"
    
RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base "

export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
