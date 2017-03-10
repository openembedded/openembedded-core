RDEPENDS_${PN} = "diffstat-native patch-native bzip2-native util-linux-native"

INHIBIT_AUTOTOOLS_DEPS = "1"

inherit native

PATCHTOOL = "patch"
EXTRA_OECONF = "--disable-nls"
EXTRA_OECONF_append_class-target = "--with-perl=perl"

do_configure () {
	oe_runconf
}

do_install_append () {
	# Dummy quiltrc file for patch.bbclass
	install -d ${D}${sysconfdir}/
	touch ${D}${sysconfdir}/quiltrc
}
