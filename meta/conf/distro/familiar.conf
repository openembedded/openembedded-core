#@TYPE: Distribution
#@NAME: Familiar Linux
#@DESCRIPTION: Distribution configuration for Familiar Linux (handhelds.org)

MAINTAINER ?= "Familiar Developers <familiar-dev@handhelds.org>"

INHERIT += "package_ipk debian multimachine"
TARGET_OS = "linux"

BOOTSTRAP_EXTRA_RDEPENDS += "familiar-version"
IMAGE_NAME = "${IMAGE_BASENAME}-${DISTRO_VERSION}-${MACHINE}"

ENABLE_BINARY_LOCALE_GENERATION ?= "1"
PARALLEL_INSTALL_MODULES = "1"
UDEV_DEVFS_RULES = "1"

DISTRO_CHECK := "${@bb.data.getVar("DISTRO_VERSION",d,1) or bb.fatal('Remove this line or set a dummy DISTRO_VERSION if you really want to build an unversioned distro')}"
