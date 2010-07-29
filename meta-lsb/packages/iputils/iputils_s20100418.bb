DESCRIPTION = "Utilities for the IP protocol, including traceroute6, \
tracepath, tracepath6, ping, ping6 and arping."
HOMEPAGE = "http://www.skbuff.net/iputils"
SECTION = "console/network"

LICENSE = "BSD & GPLv2+"

DEPENDS = "sysfsutils openssl"

PR = "r0"

SRC_URI = "http://www.skbuff.net/iputils/${PN}-${PV}.tar.bz2 \
           file://debian/fix-dead-host-ping-stats.diff \
           file://debian/add-icmp-return-codes.diff \
           file://debian/use_gethostbyname2.diff \
           file://debian/targets.diff \
           file://debian/fix-arping-timeouts.diff \
           file://debian/CVE-2010-2529.diff \
          "

# man is not compiled here, since it requires docbook-utils-native
# which is not available in poky

do_compile () {
	oe_runmake 'CC=${CC} -D_GNU_SOURCE' VPATH="${STAGING_LIBDIR}" all
}

do_install () {
	install -m 0755 -d ${D}${base_bindir} ${D}${mandir}/man8
	# SUID root programs
	install -m 4555 ping ${D}${base_bindir}/ping.${PN}
	install -m 4555 ping6 ${D}${base_bindir}/ping6.${PN}
	install -m 4555 traceroute6 ${D}${base_bindir}/
	# Other programgs
	for i in arping tracepath tracepath6; do
	  install -m 0755 $i ${D}${base_bindir}/
	done
	# Manual pages for things we build packages for
#	for i in tracepath.8 traceroute6.8 ping.8 arping.8; do
#	  install -m 0644 doc/$i ${D}${mandir}/man8/ || true
#	done
}

# Busybox also provides ping and ping6, so use update-alternatives
# Also fixup SUID bit for applications that need it
pkg_postinst_${PN}-ping () {
	chmod 4555 ${base_bindir}/ping.${PN}
	update-alternatives --install ${base_bindir}/ping ping ping.${PN} 100
}
pkg_prerm_${PN}-ping () {
	update-alternatives --remove ping ping.${PN}
}

pkg_postinst_${PN}-ping6 () {
	chmod 4555 ${base_bindir}/ping6.${PN}
	update-alternatives --install ${base_bindir}/ping6 ping6 ping6.${PN} 100
}
pkg_prerm_${PN}-ping6 () {
	update-alternatives --remove ping6 ping6.${PN}
}

pkg_postinst_${PN}-traceroute6 () {
	chmod 4555 ${base_bindir}/traceroute6
}

PACKAGES += "${PN}-ping ${PN}-ping6 ${PN}-arping ${PN}-tracepath ${PN}-tracepath6 ${PN}-traceroute6"

ALLOW_EMPTY_${PN} = "1"
RDEPENDS_${PN} += "${PN}-ping ${PN}-ping6 ${PN}-arping ${PN}-tracepath ${PN}-tracepath6 ${PN}-traceroute6"

FILES_${PN}	= ""
FILES_${PN}-ping = "${base_bindir}/ping.${PN}"
FILES_${PN}-ping6 = "${base_bindir}/ping6.${PN}"
FILES_${PN}-arping = "${base_bindir}/arping"
FILES_${PN}-tracepath = "${base_bindir}/tracepath"
FILES_${PN}-tracepath6 = "${base_bindir}/tracepath6"
FILES_${PN}-traceroute6	= "${base_bindir}/traceroute6"
FILES_${PN}-doc	= "${mandir}/man8"
