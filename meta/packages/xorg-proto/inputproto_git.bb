require xorg-proto-common.inc

PR = "r1"
PE = "1"
PV = "1.9.99.5+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/inputproto;protocol=git"
S = "${WORKDIR}/git"

BBCLASSEXTEND = "native sdk"
