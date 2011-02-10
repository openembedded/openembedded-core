require pseudo.inc

PV = "1.0+git${SRCPV}"
PR = "r18"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://static_sqlite.patch"

S = "${WORKDIR}/git"

