SUMMARY = "Security-focused ELF files checking tool"
DESCRIPTION = "This is a small set of various PaX aware and related \
utilities for ELF binaries. It can check ELF binary files and running \
processes for issues that might be relevant when using ELF binaries \
along with PaX, such as non-PIC code or executable stack and heap."
HOMEPAGE = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.bz2"

SRC_URI[md5sum] = "93a2e993ec7a83627c39ddf92988e191"
SRC_URI[sha256sum] = "eac86abe7871ee12452d221c1e7c21acb9ba78ba2cba1daa587d3c93c77acd07"

PR = "r0"

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
