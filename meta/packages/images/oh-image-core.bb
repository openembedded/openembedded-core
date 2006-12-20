PR = "r0"

export IMAGE_BASENAME = "oh-image-core"

IMAGE_FEATURES += "apps-core"

DEPENDS = "task-oh"
    
RDEPENDS = "${DISTRO_TASKS}" 

export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
