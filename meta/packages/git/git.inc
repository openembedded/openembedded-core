DESCRIPTION = "The git revision control system used by the Linux kernel developers"
SECTION = "console/utils"
LICENSE = "GPL"

SRC_URI = "http://www.kernel.org/pub/software/scm/git/git-${PV}.tar.bz2"
S = "${WORKDIR}/git-${PV}"

do_install () {
	oe_runmake install prefix=${D} bindir=${D}${bindir} \
		template_dir=${D}${datadir}/git-core/templates \
		GIT_PYTHON_DIR=${D}${datadir}/git-core/python
}

FILES_${PN} += "${datadir}/git-core"

