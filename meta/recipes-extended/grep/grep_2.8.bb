DESCRIPTION = "GNU grep utility"
HOMEPAGE = "http://savannah.gnu.org/projects/grep/"
BUGTRACKER = "http://savannah.gnu.org/bugs/?group=grep"
SECTION = "console/utils"
PRIORITY = "optional"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=8006d9c814277c1bfc4ca22af94b59ee"

PR = "r0"

SRC_URI = "${GNU_MIRROR}/grep/grep-${PV}.tar.gz"

SRC_URI[md5sum] = "cb2dfc502c5afc7a4a6e5f6cefd6850e"
SRC_URI[sha256sum] = "da48d0e20bceaacc7d05bfcbccb34c3dee0c50bd7e80de5222fd5e319809af27"

inherit autotools gettext

EXTRA_OECONF = "--disable-perl-regexp"

do_configure_prepend () {
	rm -f ${S}/m4/init.m4
}

do_install () {
	autotools_do_install
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/grep ${D}${base_bindir}/grep.${PN}
	mv ${D}${bindir}/egrep ${D}${base_bindir}/egrep.${PN}
	mv ${D}${bindir}/fgrep ${D}${base_bindir}/fgrep.${PN}
}

pkg_postinst_${PN} () {
	update-alternatives --install ${base_bindir}/grep grep grep.${PN} 100
	update-alternatives --install ${base_bindir}/egrep egrep egrep.${PN} 100
	update-alternatives --install ${base_bindir}/fgrep fgrep fgrep.${PN} 100
}

pkg_prerm_${PN} () {
	update-alternatives --remove grep grep.${PN}
	update-alternatives --remove egrep egrep.${PN}
	update-alternatives --remove fgrep fgrep.${PN}
}
