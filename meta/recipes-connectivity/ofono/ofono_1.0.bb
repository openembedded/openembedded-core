require ofono.inc

PR = "r0"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${BPN}-${PV}.tar.bz2 \
	          file://ofono"

EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"

SRC_URI[md5sum] = "09a36c923c8c0f491899aa7eebe92193"
SRC_URI[sha256sum] = "7733e1628fac6ef4d8aa3e536a5c284c3ba8e76277fc7e28b7e146c3c5ddebfa"
