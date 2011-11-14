DESCRIPTION = "NSS module for Multicast DNS name resolution"
HOMEPAGE = "http://0pointer.de/lennart/projects/nss-mdns/"
SECTION = "libs"

LICENSE = "LGPLv2.1+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = "avahi"
RDEPENDS_${PN} = "avahi-daemon"
PR = "r4"

SRC_URI = "http://0pointer.de/lennart/projects/nss-mdns/nss-mdns-${PV}.tar.gz"

SRC_URI[md5sum] = "03938f17646efbb50aa70ba5f99f51d7"
SRC_URI[sha256sum] = "1e683c2e7c3921814706d62fbbd3e9cbf493a75fa00255e0e715508d8134fa6d"
S = "${WORKDIR}/nss-mdns-${PV}"

inherit autotools

# suppress warning, but don't bother with autonamer
LEAD_SONAME = "libnss_mdns.so"
DEBIANNAME_${PN} = "libnss-mdns"

EXTRA_OECONF = "--libdir=${base_libdir} --disable-lynx --enable-avahi"

# TODO: pattern based configuration update
pkg_postinst_${PN} () {
	cat $D/etc/nsswitch.conf | grep "hosts:\s*files dns$" > /dev/null && {
		sed -i 's/hosts:\s*files dns/& mdns4/' $D/etc/nsswitch.conf
	}
}

pkg_prerm_${PN} () {
	cat /etc/nsswitch.conf | grep "hosts:\s*files dns mdns4$" > /dev/null && {
		sed -i 's/\(hosts:\s*files dns\) mdns4*/\1/' /etc/nsswitch.conf
	}
}
