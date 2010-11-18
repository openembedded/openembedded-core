DESCRIPTION = "cwautomacros: a collection of autoconf m4 macros"
SECTION = "base"
HOMEPAGE = "http://cwautomacros.berlios.de/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = http://download.berlios.de/cwautomacros/cwautomacros-${PV}.tar.bz2

PR = "r0"

SRC_URI[md5sum] = "352b295897ddb30c0d7d0acdd0b2313a"
SRC_URI[sha256sum] = "8f683713baa63e6b5c2ea72067f77cbacf0bee7d4efa907951c6bb5ac1ffd6b0"

do_install() {
	oe_runmake CWAUTOMACROSPREFIX=${D}${prefix} install
}

BBCLASSEXTEND = "native"
