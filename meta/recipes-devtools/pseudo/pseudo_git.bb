require pseudo.inc

SRCREV = "5434325fc887817ebb2bad36313d8277992cef1d"
PV = "1.0+git${SRCPV}"
PR = "r18"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://static_sqlite.patch"

S = "${WORKDIR}/git"

