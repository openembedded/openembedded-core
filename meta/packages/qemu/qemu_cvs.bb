LICENSE = "GPL"
PV = "0.8.0+cvs${SRCDATE}"
PR = "r2"

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
           file://configure.patch;patch=1 \
	   file://mouse_fix-r0.patch;patch=1 \
	   file://arm_nptl.patch;patch=1 \
	   file://pl110_rgb-r0.patch;patch=1"

S = "${WORKDIR}/qemu"

inherit autotools

