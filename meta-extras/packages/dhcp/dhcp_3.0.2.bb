SECTION = "console/network"
DESCRIPTION = "Internet Software Consortium DHCP package"
HOMEPAGE = "http://www.isc.org/"
LICENSE = "BSD"
PR = "r4"
SRC_URI = "ftp://ftp.isc.org/isc/dhcp/dhcp-3.0-history/dhcp-${PV}.tar.gz \
	   file://noattrmode.patch;patch=1 \
	   file://fixincludes.patch;patch=1 \
	   file://dhcp-3.0.3-dhclient-dbus.patch;patch=1;pnum=0 \
	   file://init-relay file://default-relay \
	   file://init-server file://default-server \
	   file://dhclient.conf file://dhcpd.conf"

do_configure() {
	./configure
}

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
}

PACKAGES =+ "dhcp-server dhcp-client dhcp-relay dhcp-omshell"
FILES_dhcp-server = "${sbindir}/dhcpd /etc/init.d/dhcp-server /etc/default/dhcp-server /etc/dhcp/dhcpd.conf"
FILES_dhcp-relay = "${sbindir}/dhcrelay /etc/init.d/dhcp-relay /etc/default/dhcp-relay"

FILES_dhcp-client = "/sbin/ /etc/dhcp/dhclient.conf"
#RDEPENDS_dhcp-client = "bash"

FILES_dhcp-omshell = "${bindir}/omshell"

CONFFILES_dhcp-server_nylon = "/etc/dhcp/dhcpd.conf"
CONFFILES_dhcp-relay_nylon = "/etc/default/dhcp-relay"
CONFFILES_dhcp-client_nylon = "/etc/dhcp/dhclient.conf"
