DESCRIPTION = "GNU Portable Threads"
HOMEPAGE = "http://www.gnu.org/software/pth/"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;beginline=12;endline=15;md5=a48af114a80c222cafd37f24370a77b1"
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
