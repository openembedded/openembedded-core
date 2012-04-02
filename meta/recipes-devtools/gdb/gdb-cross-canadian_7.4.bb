require gdb-common.inc
require gdb-cross-canadian.inc

PR = "${INC_PR}.5"

GDBPROPREFIX = "--program-prefix='${TARGET_PREFIX}'"

S = "${WORKDIR}/${BPN}-${PV}"
