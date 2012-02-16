SUMMARY = "Network monitoring tools"
DESCRIPTION = "Utilities for the IP protocol, including traceroute6, \
tracepath, tracepath6, ping, ping6 and arping."
HOMEPAGE = "http://www.skbuff.net/iputils"
SECTION = "console/network"

LICENSE = "BSD & GPLv2+"

LIC_FILES_CHKSUM = "file://ping.c;beginline=1;endline=35;md5=f9ceb201733e9a6cf8f00766dd278d82 \
                    file://tracepath.c;beginline=1;endline=10;md5=0ecea2bf60bff2f3d840096d87647f3d \
                    file://arping.c;beginline=1;endline=10;md5=ada2a6d06acc90f943bddf40d15e0541 \
                    file://tftpd.c;beginline=1;endline=32;md5=28834bf8a91a5b8a92755dbee709ef96 "

DEPENDS = "sysfsutils openssl docbook-utils-native sgmlspl-native"

PR = "r3"

SRC_URI = "http://www.skbuff.net/iputils/${BPN}-${PV}.tar.bz2 \
           file://debian/fix-dead-host-ping-stats.diff \
           file://debian/add-icmp-return-codes.diff \
           file://debian/use_gethostbyname2.diff \
           file://debian/targets.diff \
           file://debian/fix-arping-timeouts.diff \
           file://nsgmls-path-fix.patch \
          "

SRC_URI[md5sum] = "a36c25e9ec17e48be514dc0485e7376c"
SRC_URI[sha256sum] = "fd3af46c80ebb99607c2ca1f2a3608b6fe828e25bbec6e54f2afd25f6ddb6ee7"

do_compile () {
	oe_runmake 'CC=${CC} -D_GNU_SOURCE' VPATH="${STAGING_LIBDIR}:${STAGING_DIR_HOST}/${base_libdir}" all man
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
	for i in tracepath.8 traceroute6.8 ping.8 arping.8; do
	  install -m 0644 doc/$i ${D}${mandir}/man8/ || true
	done
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
