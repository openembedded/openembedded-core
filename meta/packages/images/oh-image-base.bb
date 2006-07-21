PR = "r0"

export IMAGE_BASENAME = "oh-image-base"

DEPENDS = "task-oh"
    
RDEPENDS = "task-oh-boot task-oh-boot-extras"

export IPKG_INSTALL = "${RDEPENDS}"

inherit image_ipk
LICENSE = MIT
