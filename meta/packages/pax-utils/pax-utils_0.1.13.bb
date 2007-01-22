DESCRIPTION = "Various ELF utilities"
HOMEPAGE    = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE     = "GPLv2"

SRC_URI     = "${GENTOO_MIRROR}/pax-utils-${PV}.tar.bz2;md5sum=5f09df47a16e83462384b44b75310539"
S           = "${WORKDIR}/pax-utils-${PV}"

CPPFLAGS   += "-D_GNU_SOURCE "

do_compile() {
    oe_runmake
}

do_install() {
    oe_runmake DESTDIR=${D} install
}

