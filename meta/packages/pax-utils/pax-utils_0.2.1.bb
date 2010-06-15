DESCRIPTION = "pax-utils is a small set of various PaX aware and related utilities for ELF binaries. PaX's main goal is to research various defense mechanisms against the exploitation of software bugs that give an attacker arbitrary read/write access to the attacked task's address space"
HOMEPAGE    = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE     = "GPLv2+"

SRC_URI     = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.bz2"

PR = "r0"

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

NATIVE_INSTALL_WORKS = "1"
BBCLASSEXTEND = "native"
