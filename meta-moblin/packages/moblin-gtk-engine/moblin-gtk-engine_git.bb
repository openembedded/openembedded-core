
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
LICENSE = "LGPLv2.1"
PV = "0.0+git${SRCPV}"
PR = "r1"

DEPENDS = "gtk+"

S = "${WORKDIR}/git"

inherit autotools_stage

FILES_${PN} = "${libdir}/gtk-2.0/*/engines/*.so ${datadir}/icons ${datadir}/themes"
FILES_${PN}-dev = "${libdir}/gtk-2.0/*/engines/*"
FILES_${PN}-dbg = "${libdir}/gtk-2.0/*/engines/.debug"
