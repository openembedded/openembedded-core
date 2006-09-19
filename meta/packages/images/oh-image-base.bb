PR = "r0"

export IMAGE_BASENAME = "oh-image-base"

DEPENDS = "task-oh"
    
RDEPENDS = "task-oh-boot task-oh-boot-extras"

export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
