LICENSE = "GPL"
PV = "0.8.2+cvs${SRCDATE}"
PR = "r1"

SRC_URI = "cvs://anonymous@cvs.savannah.nongnu.org/sources/qemu;method=pserver;rsh=ssh;module=qemu \
           file://configure.patch;patch=1    \
           file://qemu-sdl-cursor.patch;patch=1    \
	   file://arm_nptl.patch;patch=1     \
	   file://pl110_rgb-r0.patch;patch=1 \
	   file://qemu-pci-irq-sharing.patch;patch=1 \
	   file://compiler.patch;patch=1 \
	   file://qemu-usb-wacom-0.8.2.patch;patch=1 \
           file://qemu-usb-wacom-pressure.patch;patch=1 \
	   file://qemu-usb-wacom-buttons.patch;patch=1"

S = "${WORKDIR}/qemu"

inherit autotools

