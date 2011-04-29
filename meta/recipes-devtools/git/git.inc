DESCRIPTION = "The git revision control system used by the Linux kernel developers"
SECTION = "console/utils"
LICENSE = "GPLv2"
DEPENDS = "perl-native openssl curl zlib expat"

SRC_URI = "${KERNELORG_MIRROR}/software/scm/git/git-${PV}.tar.bz2 \
           file://autotools.patch;patch=1"
S = "${WORKDIR}/git-${PV}"

LIC_FILES_CHKSUM = "file://COPYING;md5=7c0d7ef03a7eb04ce795b0f60e68e7e1"

EXTRA_OECONF = "--with-perl=${STAGING_BINDIR_NATIVE}/perl --without-tcltk"

inherit autotools

do_install () {
	oe_runmake install DESTDIR="${D}" bindir=${bindir} gitexecdir=${gitexecdir} \
		template_dir=${datadir}/git-core/templates \
		GIT_PYTHON_DIR=${D}${datadir}/git-core/python
}

FILES_${PN} += "${datadir}/git-core"

BBCLASSEXTEND = "native"
