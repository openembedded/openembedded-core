LICENSE = "GPL"
PV = "0.8.0+cvs${SRCDATE}"

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
           file://nodocs_cvs.patch;patch=1 \
           file://configure.patch;patch=1"

S = "${WORKDIR}/qemu"

inherit autotools

