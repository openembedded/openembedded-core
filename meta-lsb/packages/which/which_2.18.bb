DESCRIPTION = "Which is a utility that prints out the full path of the \
executables that bash(1) would execute when the passed \
program names would have been entered on the shell prompt. \
It does this by using the exact same algorithm as bash."
HOMEPAGE = "http://www.xs4all.nl/~carlo17/which/"
BUGTRACKER = "n/a"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = "r0"

SRC_URI = "http://www.xs4all.nl/~carlo17/which/which-${PV}.tar.gz \
           file://fix_name_conflict_group_member.patch"

DEPENDS = "cwautomacros-native"

inherit autotools

do_configure_prepend() {
	OLD="@ACLOCAL_CWFLAGS@"
	NEW="-I ${STAGING_DIR_NATIVE}/${datadir}/cwautomacros/m4"
	sed -i "s#${OLD}#${NEW}#g" `grep -rl ${OLD} ${S}`
}
do_install_append() {
	mv ${D}/${bindir}/which ${D}/${bindir}/which.${PN}
}

pkg_postinst_${PN}() {
	if [ "${PN}" = "${BPN}" ] ; then
		update-alternatives --install ${bindir}/which which which.${PN} 100
	fi
}

pkg_prerm_${PN}() {
	if [ "${PN}" = "${BPN}" ] ; then
		update-alternatives --remove which which.${PN}
	fi
}

