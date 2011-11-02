require pseudo.inc

SRCREV = "17c2233f93692f79684792750001ee6d13e03925"
PV = "1.2+git${SRCPV}"
PR = "r20"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://oe-config.patch \
           file://static_sqlite.patch"

S = "${WORKDIR}/git"

