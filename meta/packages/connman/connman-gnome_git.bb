
SRC_URI = "git://git.kernel.org/pub/scm/network/connman/connman-gnome.git;protocol=git"
DEPENDS = "gtk+"
PV = "0.1+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

inherit autotools_stage
