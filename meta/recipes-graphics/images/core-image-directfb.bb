SUMMARY = "An image that uses DirectFB instead of X11."
LICENSE = "MIT"
PR="r0"


LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit core-image distro_features_check

REQUIRED_DISTRO_FEATURES = "directfb"
CONFLICT_DISTRO_FEATURES = "x11"

IMAGE_INSTALL += "\
	${CORE_IMAGE_BASE_INSTALL} \
	packagegroup-core-basic \
	packagegroup-core-directfb \
"
