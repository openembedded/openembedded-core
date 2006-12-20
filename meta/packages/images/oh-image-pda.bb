PR = "r10"

export IMAGE_BASENAME = "oh-image-pda"

IMAGE_FEATURES += "apps-core apps-pda"

DEPENDS = "task-oh"

RDEPENDS = "${DISTRO_TASKS}"   

export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
