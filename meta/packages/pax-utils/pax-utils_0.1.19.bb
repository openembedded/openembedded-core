DESCRIPTION = "Various ELF utilities"
HOMEPAGE    = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE     = "GPLv2"

SRC_URI     = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.bz2"
S           = "${WORKDIR}/pax-utils-${PV}"

PR = "r2"

CPPFLAGS   += "-D_GNU_SOURCE "

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

NATIVE_INSTALL_WORKS = "1"
BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "98f6b9fe17a740a8cc577255422c6103"
SRC_URI[sha256sum] = "3918628e9f2508708a1a28f5ed4cb39d07cbd5711747bbb3ddf63816d056c11e"
