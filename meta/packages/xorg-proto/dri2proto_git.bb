DESCRIPTION = "DRI2 extension headers"

require xorg-proto-common.inc

PV = "1.99.3+git${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/dri2proto;protocol=git"
S = "${WORKDIR}/git"

