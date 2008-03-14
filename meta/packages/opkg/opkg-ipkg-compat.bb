DESCRIPTION = "Files to make opkg and ipkg coexist"
RDEPENDS = "opkg ipkg ipkg-collateral"

PR = "r0"
PACKAGE_ARCH = "all"

do_compile() {
	:
}


do_install () {
	install -d ${D}${sysconfdir}
	install -d ${D}/var/lib/ipkg
	ln -sf ${sysconfdir}/ipkg.conf ${D}${sysconfdir}/opkg.conf
	ln -sf ${sysconfdir}/ipkg/ ${D}${sysconfdir}/opkg
	ln -sf /var/lib/ipkg ${D}/var/lib/ipkg
}

