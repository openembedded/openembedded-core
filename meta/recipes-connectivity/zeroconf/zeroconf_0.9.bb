SUMMARY = "IPv4 link-local address allocator"
DESCRIPTION = "Zeroconf is a program that is used to claim IPv4 \
link-local addresses. IPv4 link-local addresses are useful when setting \
up ad-hoc networking between devices without the involvement of a either \
a DHCP server or network administrator. \
These addresses are allocated from the 169.254.0.0/16 address range and \
are normally attached to each Ethernet device in your computer. \
Addresses are assigned randomly by each host and, in case of collision, \
both hosts (are supposed to) renumber."
AUTHOR = "Anand Kumria <wildfire@progsoc.uts.edu.au>"
HOMEPAGE = "http://www.progsoc.org/~wildfire/zeroconf/"
LICENSE = "GPL"
SECTION = "net"
PRIORITY = "optional"

PR = "r1"

SRC_URI = "http://www.progsoc.org/~wildfire/zeroconf/download/${PN}-${PV}.tar.gz \
           file://compilefix.patch;patch=1 \
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
