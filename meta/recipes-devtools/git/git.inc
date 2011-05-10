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
	oe_runmake install DESTDIR="${D}" bindir=${bindir} \
		template_dir=${datadir}/git-core/templates \
		GIT_PYTHON_DIR=${D}${datadir}/git-core/python
}

FILES_${PN} += "${datadir}/git-core ${libxecdir}/git-core/"
FILES_${PN}-dbg += "${libexecdir}/git-core/.debug"

# Git tools requiring perl
PACKAGES =+ "${PN}-perltools"
FILES_${PN}-perltools += " \
    ${libexecdir}/git-core/git-add--interactive \
    ${libexecdir}/git-core/git-archimport \
    ${libexecdir}/git-core/git-cvsexportcommit \
    ${libexecdir}/git-core/git-cvsimport \
    ${libexecdir}/git-core/git-cvsserver \
    ${bindir}/git-cvsserver \
    ${libexecdir}/git-core/git-difftool \
    ${libexecdir}/git-core/git-relink \
    ${libexecdir}/git-core/git-send-email \
    ${libexecdir}/git-core/git-svn \
    ${datadir}/perl \
"
RDEPENDS_${PN}-perltools = "${PN} perl perl-module-file-path findutils"

# git-tk package with gitk and git-gui
PACKAGES =+ "${PN}-tk"
#RDEPENDS_${PN}-tk = "${PN} tk tcl"
#EXTRA_OEMAKE = "TCL_PATH=${STAGING_BINDIR_CROSS}/tclsh"
FILES_${PN}-tk = " \
    ${bindir}/gitk \
    ${datadir}/gitk \
"


BBCLASSEXTEND = "native"
