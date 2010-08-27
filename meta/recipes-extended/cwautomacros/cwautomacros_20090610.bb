DESCRIPTION = "cwautomacros"
SECTION = "base"
HOMEPAGE = "http://cwautomacros.berlios.de/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = http://download.berlios.de/cwautomacros/cwautomacros-${PV}.tar.bz2

PR = "r0"

do_install() {
	oe_runmake CWAUTOMACROSPREFIX=${D}${prefix} install
}

BBCLASSEXTEND = "native"
