DESCRIPTION = "A tool for automatically generating Makefiles."
LICENSE = "GPLv2"
HOMEPAGE = "http://www.gnu.org/software/automake/"
SECTION = "devel"
PR = "r1"

SRC_URI = "${GNU_MIRROR}/automake/automake-${PV}.tar.bz2 "

inherit autotools

export AUTOMAKE = "${@bb.which('automake', bb.data.getVar('PATH', d, 1))}"

FILES_${PN} += "${datadir}/automake* ${datadir}/aclocal*"
