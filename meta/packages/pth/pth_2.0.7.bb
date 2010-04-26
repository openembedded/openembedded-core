DESCRIPTION = "GNU Portable Threads"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPL LGPL FDL"
PR = "r1"

SRC_URI = "${GNU_MIRROR}/pth/pth-${PV}.tar.gz"

PARALLEL_MAKE=""

inherit autotools binconfig

do_configure() {
	gnu-configize
	oe_runconf
}

FILES_${PN} = "${libdir}/libpth.so.*"
FILES_${PN}-dev += "${bindir}/pth-config"
