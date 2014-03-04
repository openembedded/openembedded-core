SUMMARY = "Midnight Commander is an ncurses based file manager"
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=270bbafe360e73f9840bd7981621f9c2"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

SRC_URI = "http://www.midnight-commander.org/downloads/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "eb4bdc23abd4fdfa14911d53d65c8186"
SRC_URI[sha256sum] = "5f4166fe78fbf4b42f51ed526ca7f79fea8c77d04355c2b97d4df2a6bd2a1b1a"

inherit autotools gettext pkgconfig

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"

FILES_${PN}-dbg += "${libexecdir}/mc/.debug/"

do_install_append () {
	sed -i -e '1s,#!.*perl,#!${bindir}/env perl,' ${D}${libexecdir}/mc/extfs.d/*
	sed -i -e '1s,#!.*python,#!${bindir}/env python,' ${D}${libexecdir}/mc/extfs.d/*
}

PACKAGES =+ "${BPN}-helpers-perl ${BPN}-helpers-python ${BPN}-helpers ${BPN}-fish"

SUMMARY_${BPN}-helpers-perl = "Midnight Commander Perl-based helper scripts"
FILES_${BPN}-helpers-perl = "${libexecdir}/mc/extfs.d/a+ ${libexecdir}/mc/extfs.d/apt+ \
                             ${libexecdir}/mc/extfs.d/deb ${libexecdir}/mc/extfs.d/deba \
                             ${libexecdir}/mc/extfs.d/debd ${libexecdir}/mc/extfs.d/dpkg+ \
                             ${libexecdir}/mc/extfs.d/mailfs ${libexecdir}/mc/extfs.d/patchfs \ 
                             ${libexecdir}/mc/extfs.d/rpms+ ${libexecdir}/mc/extfs.d/ulib \ 
                             ${libexecdir}/mc/extfs.d/uzip"
RDEPENDS_${BPN}-helpers-perl = "perl"

SUMMARY_${BPN}-helpers-python = "Midnight Commander Python-based helper scripts"
FILES_${BPN}-helpers-python = "${libexecdir}/mc/extfs.d/s3+ ${libexecdir}/mc/extfs.d/uc1541"
RDEPENDS_${BPN}-helpers-python = "python"

SUMMARY_${BPN}-helpers = "Midnight Commander shell helper scripts"
FILES_${BPN}-helpers = "${libexecdir}/mc/extfs.d/* ${libexecdir}/mc/ext.d/*"

SUMMARY_${BPN}-fish = "Midnight Commander Fish scripts"
FILES_${BPN}-fish = "${libexecdir}/mc/fish"
