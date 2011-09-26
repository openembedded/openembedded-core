require matchbox-theme-sato.inc

DEPENDS = "matchbox-wm-2"
SRCREV = "e3ccc08d4a680d70fd4891fca966aa6ce503065c"
PV = "0.2+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/matchbox-sato;protocol=git"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-matchbox-1 --enable-matchbox-2"
