require xorg-util-common.inc

DESCRIPTION = "X autotools macros"

PR = "r1"

SRC_URI = "${XORG_MIRROR}/individual/util/${XORG_PN}-1.1.5.tar.gz \
           file://unbreak_cross_compile.patch;patch=1 "
S = "${WORKDIR}/${XORG_PN}-1.1.5"

