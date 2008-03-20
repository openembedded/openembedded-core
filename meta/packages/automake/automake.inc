DESCRIPTION = "A tool for automatically generating Makefiles."
LICENSE = "GPL"
HOMEPAGE = "http://www.gnu.org/software/automake/"
SECTION = "devel"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/automake-${PV}"
PR = "r1"

SRC_URI = "${GNU_MIRROR}/automake/automake-${PV}.tar.bz2 "

S = "${WORKDIR}/automake-${PV}"

inherit autotools

export AUTOMAKE = "${@bb.which('automake', bb.data.getVar('PATH', d, 1))}"

FILES_${PN} += "${datadir}/automake* ${datadir}/aclocal*"
