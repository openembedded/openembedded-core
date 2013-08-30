require libdrm.inc

SRC_URI = "git://anongit.freedesktop.org/git/mesa/drm"

S = "${WORKDIR}/git"

DEFAULT_PREFERENCE = "-1"

SRCREV = "e01d68f9f3acfc35fe164283904b5d058c2ab378"
PV = "2.4.40+git${SRCPV}"
PR = "${INC_PR}.0"

