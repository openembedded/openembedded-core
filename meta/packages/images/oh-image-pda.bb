PR = "r10"

export IMAGE_BASENAME = "oh-image-pda"

DEPENDS = "task-oh"

RDEPENDS = "\
    task-oh-boot \
    task-oh-boot-extras \
    task-oh-base \
    task-oh-standard \
    ${@base_conditional("DISTRO_TYPE", "debug", "task-oh-devtools", "",d)} \
    ${@base_conditional("DISTRO_TYPE", "debug", "task-oh-testapps", "",d)} "

export IPKG_INSTALL = "${RDEPENDS}"

inherit image_ipk
LICENSE = MIT
