DESCRIPTION = "manage symlinks in /etc/rcN.d."
SECTION = "base"
PRIORITY = "standard"
PACKAGE_ARCH = "all"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://update-rc.d;beginline=5;endline=15;md5=148a48321b10eb37c1fa3ee02b940a75"

PR = "r3"

SRC_URI = "${HANDHELDS_CVS};module=apps/update-rc.d;tag=r0_7 \
           file://add-verbose.patch;apply=yes"

S = "${WORKDIR}/update-rc.d"


do_compile() {
}

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${S}/update-rc.d ${D}${sbindir}/update-rc.d
}

BBCLASSEXTEND = "native"
