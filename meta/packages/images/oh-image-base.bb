PR = "r0"

export IMAGE_BASENAME = "oh-image-base"

DEPENDS = "task-oh"
    
RDEPENDS = "${DISTRO_TASKS}"

export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
