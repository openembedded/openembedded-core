SUMMARY = "manage symlinks in /etc/rcN.d."
DESCRIPTION = "update-rc.d is a utilities that allows the management of symlinks to the initscripts in the /etc/rcN.d directory structure."
SECTION = "base"
PRIORITY = "standard"
PACKAGE_ARCH = "all"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://update-rc.d;beginline=5;endline=15;md5=148a48321b10eb37c1fa3ee02b940a75"

PR = "r3"

SRC_URI = "git://github.com/philb/update-rc.d.git;tag=update-rc.d_${PV};protocol=git \
           file://add-verbose.patch;striplevel=1"

S = "${WORKDIR}/git"


do_compile() {
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/update-rc.d ${D}${sbindir}/update-rc.d
}

BBCLASSEXTEND = "native"
