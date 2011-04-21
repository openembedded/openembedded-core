SECTION = "console/network"
DESCRIPTION = "Internet Software Consortium DHCP package"
HOMEPAGE = "http://www.isc.org/"

LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=4;md5=bb6fd41f5895b67088ebea61ad365e74"

SRC_URI = "ftp://ftp.isc.org/isc/dhcp/dhcp-${PV}.tar.gz \
	   file://init-relay file://default-relay \
	   file://init-server file://default-server \
	   file://dhclient.conf file://dhcpd.conf"

inherit autotools

TARGET_CFLAGS += "-D_GNU_SOURCE"

do_compile() {
	make RANLIB=${RANLIB} PREDEFINES='-D_PATH_DHCPD_DB=\"/var/lib/dhcp/dhcpd.leases\" \
        -D_PATH_DHCLIENT_DB=\"/var/lib/dhcp/dhclient.leases\" \
        -D_PATH_DHCLIENT_SCRIPT=\"/sbin/dhclient-script\" \
        -D_PATH_DHCPD_CONF=\"/etc/dhcp/dhcpd.conf\" \
        -D_PATH_DHCLIENT_CONF=\"/etc/dhcp/dhclient.conf\"'
}

do_install() {
	make -e DESTDIR=${D} USRMANDIR=${mandir}/man1 ADMMANDIR=${mandir}/man8 FFMANDIR=${mandir}/man5 LIBMANDIR=${mandir}/man3 LIBDIR=${libdir} INCDIR=${includedir} install
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${sysconfdir}/default
	install -d ${D}${sysconfdir}/dhcp
	install -m 0755 ${WORKDIR}/init-relay ${D}${sysconfdir}/init.d/dhcp-relay
	install -m 0644 ${WORKDIR}/default-relay ${D}${sysconfdir}/default/dhcp-relay
	install -m 0755 ${WORKDIR}/init-server ${D}${sysconfdir}/init.d/dhcp-server
	install -m 0644 ${WORKDIR}/default-server ${D}${sysconfdir}/default/dhcp-server
	install -m 0644 ${WORKDIR}/dhclient.conf ${D}${sysconfdir}/dhcp/dhclient.conf
	install -m 0644 ${WORKDIR}/dhcpd.conf ${D}${sysconfdir}/dhcp/dhcpd.conf
	install -d ${D}${base_sbindir}/
	mv ${D}${sbindir}/dhclient ${D}${base_sbindir}/
	install -m 0755 ${S}/client/scripts/linux ${D}${base_sbindir}/dhclient-script
}

PACKAGES += "dhcp-server dhcp-client dhcp-relay dhcp-omshell"
FILES_${PN} = ""
FILES_dhcp-server = "${sbindir}/dhcpd ${sysconfdir}/init.d/dhcp-server ${sysconfdir}/default/dhcp-server ${sysconfdir}/dhcp/dhcpd.conf"
FILES_dhcp-relay = "${sbindir}/dhcrelay ${sysconfdir}/init.d/dhcp-relay ${sysconfdir}/default/dhcp-relay"

FILES_dhcp-client = "${base_sbindir}/dhclient ${base_sbindir}/dhclient-script ${sysconfdir}/dhcp/dhclient.conf"
RDEPENDS_dhcp-client = "bash"

FILES_dhcp-omshell = "${bindir}/omshell"
