SECTION = "console/utils"
SRC_URI = "http://fresh.t-systems-sfr.com/linux/src/setserial-2.17.tar.gz"
LICENSE = "GPL"
PR = "r1"
inherit autotools

do_install() {
	install -d ${D}${base_bindir}
	install -d ${D}/usr/man/man8
	install -d ${D}${mandir}
	autotools_do_install
}
