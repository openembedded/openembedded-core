SUMMARY = "Security-focused ELF files checking tool"
DESCRIPTION = "This is a small set of various PaX aware and related \
utilities for ELF binaries. It can check ELF binary files and running \
processes for issues that might be relevant when using ELF binaries \
along with PaX, such as non-PIC code or executable stack and heap."
HOMEPAGE = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.xz"

SRC_URI[md5sum] = "8ae7743ad11500f7604f2e817221d877"
SRC_URI[sha256sum] = "1ac4cee9a9ca97a723505eb29a25e50adeccffba3f0f0ef4f035cf082caf3b84"

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
