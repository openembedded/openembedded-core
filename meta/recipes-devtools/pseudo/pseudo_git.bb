require pseudo.inc

SRCREV = "1d3e67cb168c3459e67a0b29f071ca30ed17dadc"
PV = "1.1.1+git${SRCPV}"
PR = "r19"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://oe-config.patch \
           file://static_sqlite.patch"

S = "${WORKDIR}/git"

