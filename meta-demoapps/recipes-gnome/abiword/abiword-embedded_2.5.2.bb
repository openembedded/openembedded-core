require abiword-2.5.inc

EXTRA_OECONF += "--enable-embedded"

S = "${WORKDIR}/abiword-${PV}"

RCONFLICTS = "abiword"
RPROVIDES += "abiword"


