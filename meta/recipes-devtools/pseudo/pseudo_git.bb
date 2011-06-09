require pseudo.inc

SRCREV = "c2f7c5ad8ef0f9c94a2a8382c109c8c6e16c8b18"
PV = "1.1.1+git${SRCPV}"
PR = "r19"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://oe-config.patch \
           file://static_sqlite.patch"

S = "${WORKDIR}/git"

