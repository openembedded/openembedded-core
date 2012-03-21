SUMMARY = "Pattern matching utilities"
DESCRIPTION = "The GNU versions of commonly used grep utilities.  The grep command searches one or more input \
files for lines containing a match to a specified pattern."
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

PR = "r1"

SRC_URI = "${GNU_MIRROR}/grep/grep-${PV}.tar.bz2 \
           file://uclibc-fix.patch"

SRC_URI[md5sum] = "52202fe462770fa6be1bb667bd6cf30c"
SRC_URI[sha256sum] = "38c8a2bb9223d1fb1b10bdd607cf44830afc92fd451ac4cd07619bf92bdd3132"

inherit autotools gettext

EXTRA_OECONF = "--disable-perl-regexp --disable-ncurses"

do_configure_prepend () {
	rm -f ${S}/m4/init.m4
}

do_install () {
	autotools_do_install
	install -d ${D}${base_bindir}
	mv ${D}${bindir}/grep ${D}${base_bindir}/grep.${PN}
	mv ${D}${bindir}/egrep ${D}${base_bindir}/egrep.${PN}
	mv ${D}${bindir}/fgrep ${D}${base_bindir}/fgrep.${PN}
	rmdir ${D}${bindir}/
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
