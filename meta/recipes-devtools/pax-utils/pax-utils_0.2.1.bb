SUMMARY = "Security-focused ELF files checking tool"
DESCRIPTION = "This is a small set of various PaX aware and related utilities for ELF binaries. It can check ELF \
binary files and running processes for issues that might be relevant when using ELF binaries along with PaX, \
such as non-PIC code or executable stack and heap."
HOMEPAGE    = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE     = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"
SRC_URI     = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.bz2"

SRC_URI[md5sum] = "2bf53234580e02294453a40c864f5871"
SRC_URI[sha256sum] = "6a5beefb686e425eb6829bc716d2b32b64c2e854287c212c9853bc2beb705c3a"

PR = "r0"

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"
