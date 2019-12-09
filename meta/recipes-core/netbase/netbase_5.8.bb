SUMMARY = "Basic TCP/IP networking support"
DESCRIPTION = "This package provides the necessary infrastructure for basic TCP/IP based networking"
HOMEPAGE = "http://packages.debian.org/netbase"
SECTION = "base"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://debian/copyright;md5=3dd6192d306f582dee7687da3d8748ab"
PE = "1"

SRC_URI = "${DEBIAN_MIRROR}/main/n/${BPN}/${BPN}_${PV}.tar.xz"

SRC_URI[md5sum] = "ca2e339861b64370779341b9696ee3df"
SRC_URI[sha256sum] = "4a9035eb379c4f7f7d424e5580b219b79566fa366c812542691ae337486cfb8e"

UPSTREAM_CHECK_URI = "${DEBIAN_MIRROR}/main/n/netbase/"
do_install () {
	install -d ${D}/${mandir}/man8 ${D}${sysconfdir}
	install -m 0644 etc-rpc ${D}${sysconfdir}/rpc
	install -m 0644 etc-protocols ${D}${sysconfdir}/protocols
	install -m 0644 etc-services ${D}${sysconfdir}/services
}
