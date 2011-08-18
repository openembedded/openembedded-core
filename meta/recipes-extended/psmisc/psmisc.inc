LICENSE = "GPL"
SUMMARY = "Utilities for managing processes on your system"
DESCRIPTION = "The psmisc package contains utilities for managing processes on your \
system: pstree, killall and fuser.  The pstree command displays a tree \
structure of all of the running processes on your system.  The killall \
command sends a specified signal (SIGTERM if nothing is specified) to \
processes identified by name.  The fuser command identifies the PIDs \
of processes that are using specified files or filesystems."
SECTION = "base"
DEPENDS = "ncurses virtual/libintl"

SRC_URI = "${SOURCEFORGE_MIRROR}/psmisc/psmisc-${PV}.tar.gz \
	   file://libintl-link.patch"
S = "${WORKDIR}/psmisc-${PV}"

inherit autotools gettext

ALLOW_EMPTY = "1"

PACKAGES =+ "fuser fuser-doc killall killall-doc pstree pstree-doc "

FILES_${PN} = ""
RDEPENDS_${PN} = "fuser killall pstree"

FILES_fuser = "${bindir}/fuser.${PN}"
FILES_fuser-doc = "${mandir}/man1/fuser*"

FILES_killall = "${bindir}/killall.${PN}"
FILES_killall-doc = "${mandir}/man1/killall*"

FILES_pstree = "${bindir}/pstree"
FILES_pstree-doc = "${mandir}/man1/pstree*"

do_install_append() {
	mv ${D}${bindir}/killall ${D}${bindir}/killall.${PN}
	mv ${D}${bindir}/fuser ${D}${bindir}/fuser.${PN}
}

pkg_postinst_killall() {
	update-alternatives --install ${bindir}/killall killall killall.${PN} 90
}

pkg_postrm_killall() {
	update-alternatives --remove ${bindir}/killall killall.${PN}
}

pkg_postinst_fuser() {
	update-alternatives --install ${bindir}/fuser fuser fuser.${PN} 90
}

pkg_postrm_fuser() {
    update-alternatives --remove ${bindir}/fuser fuser.${PN}
}

