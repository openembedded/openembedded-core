require gdb-cross.inc

SRC_URI += "file://sim-install-6.6.patch"

PR = "${INC_PR}.0"

S = "${WORKDIR}/${BPN}-${PV}"
