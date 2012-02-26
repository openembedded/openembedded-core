require libdrm.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"

S = "${WORKDIR}/git"

SRCREV = "3f3c5be6f908272199ccf53f108b1124bfe0a00e"
PV = "2.4.15+git${SRCPV}"
PR = "r1"
