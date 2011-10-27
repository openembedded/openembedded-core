require gdb-common.inc
require gdb-cross-canadian.inc

PR = "${INC_PR}.1"

GDBPROPREFIX = "--program-prefix='${TARGET_PREFIX}'"
EXPAT = "--with-expat"

S = "${WORKDIR}/${BPN}-7.3"
