require ofono.inc

PR = "r0"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${BPN}-${PV}.tar.bz2 \
	          file://ofono"

EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"

SRC_URI[md5sum] = "dbe381b739c0e7830e6c075f81c8e168"
SRC_URI[sha256sum] = "02a9f1f7c1b8eeb8868290d119fe5dc90b0b5c0b6199bed5293dc06a8bf7ebeb"
