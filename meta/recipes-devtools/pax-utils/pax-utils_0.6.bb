SUMMARY = "Security-focused ELF files checking tool"
DESCRIPTION = "This is a small set of various PaX aware and related \
utilities for ELF binaries. It can check ELF binary files and running \
processes for issues that might be relevant when using ELF binaries \
along with PaX, such as non-PIC code or executable stack and heap."
HOMEPAGE = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.xz"

SRC_URI[md5sum] = "376ec23930a55a8d3e167d50e23bb66e"
SRC_URI[sha256sum] = "1ffd4bf7bf3f8bb404007dd1617edfb0297e4c65a2c1153712e4a76875038784"
PR = "r0"

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
