LICENSE = "GPL"
DESCRIPTION = "procfs tools"
SECTION = "base"
PRIORITY = "required"
DEPENDS = "ncurses"
PR = "r3"

SRC_URI = "${SOURCEFORGE_MIRROR}/psmisc/psmisc-${PV}.tar.gz \
	   file://gettext.patch;patch=1"
S = "${WORKDIR}/psmisc-${PV}"

inherit autotools gettext

ALLOW_EMPTY = "1"

PACKAGES = "psmisc-dbg ${PN} fuser psmisc-doc \
	    killall killall-doc \
	    pstree pstree-doc"

FILES_${PN} = ""
RDEPENDS_${PN} = "fuser killall pstree"

FILES_fuser = "${bindir}/fuser"

FILES_killall = "${bindir}/killall.${PN}"

FILES_pstree = "${bindir}/pstree"

do_install_append() {
	mv ${D}${bindir}/killall ${D}${bindir}/killall.${PN}
}

pkg_postinst_killall() {
	update-alternatives --install ${bindir}/killall killall killall.${PN} 90
}

pkg_postrm_killall() {
	update-alternatives --remove ${bindir}/killall killall.${PN}
}
