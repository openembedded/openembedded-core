DESCRIPTION = "A console URL download utility featuring HTTP, FTP, and more."
SECTION = "console/network"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
DEPENDS = "openssl"

INC_PR = "r12"

inherit autotools gettext update-alternatives

EXTRA_OECONF = "--with-libc --enable-ipv6 --with-ssl=openssl"

do_install_append () {
	mv ${D}${bindir}/wget ${D}${bindir}/wget.${PN}
}

ALTERNATIVE_NAME = "wget"
ALTERNATIVE_LINK = "${bindir}/wget"
ALTERNATIVE_PATH = "${bindir}/wget.${PN}"
ALTERNATIVE_PRIORITY = "100"
