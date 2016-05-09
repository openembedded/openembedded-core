SUMMARY = "An image that uses DirectFB instead of X11"
LICENSE = "MIT"

inherit core-image distro_features_check

REQUIRED_DISTRO_FEATURES = "directfb"
CONFLICT_DISTRO_FEATURES = "x11"

IMAGE_INSTALL += "\
	${CORE_IMAGE_BASE_INSTALL} \
	packagegroup-core-full-cmdline \
	packagegroup-core-directfb \
"
