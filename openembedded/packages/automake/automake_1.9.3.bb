DESCRIPTION = "A tool for automatically generating Makefiles."
LICENSE = "GPL"
HOMEPAGE = "http://www.gnu.org/software/automake/"
SECTION = "devel"
PR = "r5"

SRC_URI = "${GNU_MIRROR}/automake/automake-${PV}.tar.bz2 \
	${@['file://path_prog_fixes.patch;patch=1', ''][bb.data.inherits_class('native', d)]}"
S = "${WORKDIR}/automake-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/automake-${PV}"

inherit autotools

export AUTOMAKE = "${@bb.which('automake', bb.data.getVar('PATH', d, 1))}"
FILES_${PN} += "${datadir}/automake* ${datadir}/aclocal*"
RDEPENDS_${PN} += "autoconf perl"

do_install () {
	oe_runmake 'DESTDIR=${D}' install
	install -d ${D}${datadir}
	if [ ! -e ${D}${datadir}/aclocal ]; then
		ln -sf aclocal-1.9 ${D}${datadir}/aclocal
	fi
	if [ ! -e ${D}${datadir}/automake ]; then
		ln -sf automake-1.9 ${D}${datadir}/automake
	fi
}
