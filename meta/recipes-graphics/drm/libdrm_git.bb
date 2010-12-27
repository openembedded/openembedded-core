require libdrm.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm;protocol=git"

S = ${WORKDIR}/git

PV = "2.4.15+git${SRCPV}"
PR = "r0"
