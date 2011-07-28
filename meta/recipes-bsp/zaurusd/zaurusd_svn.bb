DESCRIPTION = "Daemon to handle device specifc features."
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"
DEPENDS = "tslib"
RDEPENDS_${PN} = "xrandr"
SRCREV = "426"
PV = "0.0+svnr${SRCPV}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=zaurusd;proto=http \
	file://fix_makefile.patch"

S = "${WORKDIR}/${BPN}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig update-rc.d

INITSCRIPT_NAME = "zaurusd"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
