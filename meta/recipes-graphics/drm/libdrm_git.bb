require libdrm.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"

S = "${WORKDIR}/git"

SRCREV = "1b1a4f0a779f7ab2ba5673b9c9fe2a37047fe765"
PV = "2.4.37+git${SRCPV}"
PR = "${INC_PR}.0"

