require contacts.inc

PV = "0.9+git${SRCPV}"
PR = "r1"

SRC_URI =+ "git://git.gnome.org/${PN};protocol=git"

S = "${WORKDIR}/git"


