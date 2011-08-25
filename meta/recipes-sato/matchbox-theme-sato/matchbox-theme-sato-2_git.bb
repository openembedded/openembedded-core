require matchbox-theme-sato.inc

DEPENDS = "matchbox-wm-2"
SRCREV = "f72cf4ed7d71ad9e47b0f2d3dbc593bc2f3e76f8"
PV = "0.2+git${SRCPV}"

SRC_URI = "git://git.yoctoproject.org/matchbox-sato;protocol=git \
          file://png_rename.patch"

S = "${WORKDIR}/git"

EXTRA_OECONF = "--disable-matchbox-1 --enable-matchbox-2"
