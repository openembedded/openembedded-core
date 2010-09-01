DESCRIPTION = "NSS module for Multicast DNS name resolution"
HOMEPAGE = "http://0pointer.de/lennart/projects/nss-mdns/"
SECTION = "libs"
PRIORITY = "optional"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "avahi"
RDEPENDS = "avahi-daemon"
PR = "r0"

SRC_URI = "http://0pointer.de/lennart/projects/nss-mdns/nss-mdns-${PV}.tar.gz"
S = "${WORKDIR}/nss-mdns-${PV}"

inherit autotools

# suppress warning, but don't bother with autonamer
LEAD_SONAME = "libnss_mdns.so"
DEBIANNAME_${PN} = "libnss-mdns"

EXTRA_OECONF = "--libdir=/lib --disable-lynx --enable-avahi"

pkg_postinst () {
	cat /etc/nsswitch.conf | grep "hosts:\s*files dns$" > /dev/null && {
		cat /etc/nsswitch.conf | sed 's/hosts:\s*files dns/& mdns4/' > /tmp/nsswitch.conf
		mv /tmp/nsswitch.conf /etc/nsswitch.conf
	}
}

pkg_prerm () {
	cat /etc/nsswitch.conf | grep "hosts:\s*files dns mdns$" > /dev/null && {
		cat /etc/nsswitch.conf | sed 's/\(hosts:\s*files dns\) mdns4*/\1/' > /tmp/nsswitch.conf
		mv /tmp/nsswitch.conf /etc/nsswitch.conf
	}
}
