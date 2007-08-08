require exmap-console.inc

PV = "0.4+svn${SRCDATE}"
PR = "r13"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=exmap-console;proto=http"

S = "${WORKDIR}/exmap-console"

MYPV := "${PV}"
PV_kernel-module-exmap = "${MYPV}-${KERNEL_VERSION}"
