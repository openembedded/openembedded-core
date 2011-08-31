require gdb-cross.inc

SRC_URI += "file://sim-install-6.6.patch"
EXPAT = "--with-expat"

PR = "${INC_PR}.0"

S = "${WORKDIR}/${BPN}-7.3"
