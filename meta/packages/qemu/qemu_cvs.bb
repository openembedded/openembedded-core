LICENSE = "GPL"
PV = "0.8.2+cvs${SRCDATE}"

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
           file://configure.patch;patch=1 \
	   file://arm_nptl.patch;patch=1 \
	   file://pl110_rgb-r0.patch;patch=1 \
	   file://qemu-usb-wacom.patch;patch=1"

S = "${WORKDIR}/qemu"

inherit autotools

