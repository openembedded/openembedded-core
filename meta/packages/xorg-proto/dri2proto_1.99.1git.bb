DESCRIPTION = "DRI2 extension headers"

require xorg-proto-common.inc

PV = "1.99.1+git${SRCREV}"

SRC_URI = "git://git.freedesktop.org/git/xorg/proto/dri2proto;protocol=git"
S = "${WORKDIR}/git"

