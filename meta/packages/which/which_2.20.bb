DESCRIPTION = "which - shows the full path of (shell) commands."
SECTION     = "libs"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504\
                    file://which.c;beginline=1;endline=17;md5=a9963693af2272e7a8df6f231164e7a2"
HOMEPAGE = "http://ftp.gnu.org/gnu/which/"
DEPENDS     = "cwautomacros-native"

inherit autotools

PR = "r0"

SRC_URI = "http://ftp.gnu.org/gnu/which/which-${PV}.tar.gz \
           file://remove-declaration.patch"

do_configure_prepend() {
	sed -i -e 's%@ACLOCAL_CWFLAGS@%-I ${STAGING_DIR_NATIVE}/usr/share/cwautomacros/m4%g' ${S}/Makefile.am ${S}/tilde/Makefile.am
}

do_install() {
	autotools_do_install
	mv ${D}${bindir}/which ${D}${bindir}/which.${PN}
}

pkg_postinst_${PN} () {
        if [ "${PN}" = "${BPN}" ] ; then
                update-alternatives --install ${bindir}/which which which.${PN} 100
        fi
}

pkg_prerm_${PN} () {
	if [ "${PN}" = "${BPN}" ] ; then
		update-alternatives --remove which which.${PN}
	fi
}
