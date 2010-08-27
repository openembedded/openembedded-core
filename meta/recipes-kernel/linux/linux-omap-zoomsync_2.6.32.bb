require linux.inc

DESCRIPTION = "Linux kernel for OMAPZoom2 machine"
KERNEL_IMAGETYPE = "uImage"

COMPATIBLE_MACHINE = "zoom2"

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_zoom2 = "1"

SRCREV = "015cbaf1035cd9a61d33a27de2a22902555db3c5"
OEV = "oe1+gitr${SRCREV}"

PV = "2.6.32.7-${OEV}"

SRC_URI = "git://dev.omapzoom.org/pub/scm/integration/kernel-omap3.git;protocol=git \
	   file://defconfig"

SRC_URI_append = " \
"

S = "${WORKDIR}/git"

PACKAGES =+ "omap-dss-doc"
FILES_omap-dss-doc = "/boot/DSS"

