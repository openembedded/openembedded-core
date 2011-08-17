require ofono.inc

PR = "r1"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${BPN}-${PV}.tar.bz2 \
	          file://ofono"

EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"

SRC_URI[md5sum] = "b2656fd0bbf33f926fc86c1e8915d697"
SRC_URI[sha256sum] = "f8f8dd917847a007e4d441b949efc4d28dc3644526d5293016844c2536c65ff9"
