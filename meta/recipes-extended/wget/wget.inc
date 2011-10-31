DESCRIPTION = "A console URL download utility featuring HTTP, FTP, and more."
SECTION = "console/network"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

INC_PR = "r10"

inherit autotools gettext update-alternatives

# Disable checking for SSL since that searches the system paths
EXTRA_OECONF = "--with-libc --enable-ipv6 --without-ssl"

do_install_append () {
	mv ${D}${bindir}/wget ${D}${bindir}/wget.${PN}
}

ALTERNATIVE_NAME = "wget"
ALTERNATIVE_LINK = "${base_bindir}/wget"
ALTERNATIVE_PATH = "${base_bindir}/wget.${PN}"
ALTERNATIVE_PRIORITY = "100"
