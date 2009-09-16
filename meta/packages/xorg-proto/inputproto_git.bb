require xorg-proto-common.inc

PR = "r1"
PE = "1"
PV = "1.9.99.12+git${SRCPV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/proto/inputproto;protocol=git"
S = "${WORKDIR}/git"

DEPENDS += "util-macros"

BBCLASSEXTEND = "native nativesdk"
