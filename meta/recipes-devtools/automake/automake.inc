SUMMARY = "A GNU tool for automatically generating Makefiles."
DESCRIPTION = "Automake is a tool for automatically generating `Makefile.in' files compliant with the GNU Coding \
Standards. Automake requires the use of Autoconf."
LICENSE = "GPLv2"
HOMEPAGE = "http://www.gnu.org/software/automake/"
SECTION = "devel"
PR = "r4"

SRC_URI = "${GNU_MIRROR}/automake/automake-${PV}.tar.bz2 "

inherit autotools

export AUTOMAKE = "${@bb.which('automake', bb.data.getVar('PATH', d, 1))}"

FILES_${PN} += "${datadir}/automake* ${datadir}/aclocal*"
