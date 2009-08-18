require tasks.inc

SRC_URI = "git://git.gnome.org/${PN};protocol=git"

PV = "0.13+git${SRCPV}"
S = "${WORKDIR}/git"
