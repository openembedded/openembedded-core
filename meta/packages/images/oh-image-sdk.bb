PR = "r9"

export IMAGE_BASENAME = "oh-image-sdk"

IMAGE_FEATURES += "apps-core apps-pda dev-tools dev-pkgs dbg-pkgs"

DEPENDS = "\
    task-oh \
    task-oh-sdk"
    
RDEPENDS = "${DISTRO_TASKS}"
export PACKAGE_INSTALL = "${RDEPENDS}"

inherit image
LICENSE = MIT
