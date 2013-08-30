require oprofile.inc

SRCREV = "88f43190d412d28ebf5c75a76ba20343d0fe4c41"
PV = "0.9.7+git${SRCPV}"
PR = "${INC_PR}.0"

SRC_URI += "git://oprofile.git.sourceforge.net/gitroot/${BPN}/${BPN}"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"
