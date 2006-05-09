DESCRIPTION = "IPv4 link-local address allocator"
AUTHOR = "Anand Kumria <wildfire@progsoc.uts.edu.au>"
HOMEPAGE = "http://www.progsoc.org/~wildfire/zeroconf/"
LICENSE = "GPL"
SECTION = "net"
PRIORITY = "optional"

PR = "r0"

SRC_URI = "http://www.progsoc.org/~wildfire/zeroconf/download/${PN}-${PV}.tar.gz \
	   file://zeroconf-default \
	   file://debian-zeroconf"

do_install () {
	install -d ${D}${sbindir}
	install -d ${D}${sysconfdir}/network/if-up.d
	install -d ${D}${sysconfdir}/default
	install -c -m 755 ${S}/zeroconf ${D}${sbindir}/zeroconf
	install -c -m 755 ${WORKDIR}/debian-zeroconf ${D}${sysconfdir}/network/if-up.d/zeroconf
	install -c ${WORKDIR}/zeroconf-default ${D}${sysconfdir}/default/zeroconf
}
