SUMMARY = "Displays the full path of executables that bash would execute"
DESCRIPTION = "Which is a utility that prints out the full path of the \
executables that bash(1) would execute when the passed \
program names would have been entered on the shell prompt. \
It does this by using the exact same algorithm as bash."
HOMEPAGE = "http://www.xs4all.nl/~carlo17/which/"
BUGTRACKER = "n/a"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

PR = "r1"

SRC_URI = "http://www.xs4all.nl/~carlo17/which/which-${PV}.tar.gz \
           file://fix_name_conflict_group_member.patch"

SRC_URI[md5sum] = "42d51938e48b91f6e19fabf216f5c3e9"
SRC_URI[sha256sum] = "9445cd7e02ec0c26a44fd56098464ded064ba5d93dd2e15ec12410ba56b2e544"

DEPENDS = "cwautomacros-native"

inherit autotools update-alternatives

do_configure_prepend() {
	OLD="@ACLOCAL_CWFLAGS@"
	NEW="-I ${STAGING_DIR_NATIVE}/${datadir}/cwautomacros/m4"
	sed -i "s#${OLD}#${NEW}#g" `grep -rl ${OLD} ${S}`
}
do_install_append() {
	mv ${D}/${bindir}/which ${D}/${bindir}/which.${PN}
}

ALTERNATIVE_NAME = "which"
ALTERNATIVE_PATH = "which.${PN}"
ALTERNATIVE_PRIORITY = "100"

