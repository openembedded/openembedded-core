require ofono.inc

PR = "r1"

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/ofono/${BPN}-${PV}.tar.bz2 \
	          file://ofono"

EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"

SRC_URI[md5sum] = "a54935a2a86b90300410c6c033284f6c"
SRC_URI[sha256sum] = "3a70608c17f3dca34c1e057aacdf836021322dfdcaf9edc777119eafe48076b3"
