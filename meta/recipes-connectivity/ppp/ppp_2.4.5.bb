SUMMARY = "Point-to-Point Protocol (PPP) support"
DESCRIPTION = "ppp (Paul's PPP Package) is an open source package which implements \
the Point-to-Point Protocol (PPP) on Linux and Solaris systems."
SECTION = "console/network"
HOMEPAGE = "http://samba.org/ppp/"
BUGTRACKER = "http://ppp.samba.org/cgi-bin/ppp-bugs"
DEPENDS = "libpcap"
LICENSE = "BSD & GPLv2+ & LGPLv2+ & PD"
LIC_FILES_CHKSUM = "file://pppd/ccp.c;beginline=1;endline=29;md5=e2c43fe6e81ff77d87dc9c290a424dea \
                    file://pppd/plugins/passprompt.c;beginline=1;endline=10;md5=3bcbcdbf0e369c9a3e0b8c8275b065d8 \
                    file://pppd/tdb.c;beginline=1;endline=27;md5=4ca3a9991b011038d085d6675ae7c4e6 \
                    file://chat/chat.c;beginline=1;endline=15;md5=0d374b8545ee5c62d7aff1acbd38add2"
PR = "r1"

SRC_URI = "http://ppp.samba.org/ftp/ppp/ppp-${PV}.tar.gz \
           file://makefile.patch \
           file://cifdefroute.patch \
           file://pppd-resolv-varrun.patch \
           file://enable-ipv6.patch \
           file://makefile-remove-hard-usr-reference.patch \
           file://pon \
           file://poff \
           file://init \
           file://ip-up \
           file://ip-down \
           file://08setupdns \
           file://92removedns"

SRC_URI[md5sum] = "4621bc56167b6953ec4071043fe0ec57"
SRC_URI[sha256sum] = "43317afec9299f9920b96f840414c977f0385410202d48e56d2fdb8230003505"

inherit autotools

EXTRA_OEMAKE = "STRIPPROG=${STRIP} MANDIR=${D}${datadir}/man/man8 INCDIR=${D}/usr/include LIBDIR=${D}/usr/lib/pppd/${PV} BINDIR=${D}/usr/sbin"
EXTRA_OECONF = "--disable-strip"

do_install_append () {
	make install-etcppp ETCDIR=${D}/${sysconfdir}/ppp
	mkdir -p ${D}${bindir}/ ${D}${sysconfdir}/init.d
	mkdir -p ${D}${sysconfdir}/ppp/ip-up.d/
	mkdir -p ${D}${sysconfdir}/ppp/ip-down.d/
	install -m 0755 ${WORKDIR}/pon ${D}${bindir}/pon
	install -m 0755 ${WORKDIR}/poff ${D}${bindir}/poff
	install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/ppp
	install -m 0755 ${WORKDIR}/ip-up ${D}${sysconfdir}/ppp/
	install -m 0755 ${WORKDIR}/ip-down ${D}${sysconfdir}/ppp/
	install -m 0755 ${WORKDIR}/08setupdns ${D}${sysconfdir}/ppp/ip-up.d/
	install -m 0755 ${WORKDIR}/92removedns ${D}${sysconfdir}/ppp/ip-down.d/
	rm -rf ${D}/${mandir}/man8/man8
}

CONFFILES_${PN} = "${sysconfdir}/ppp/pap-secrets ${sysconfdir}/ppp/chap-secrets ${sysconfdir}/ppp/options"
PACKAGES += "ppp-oa ppp-oe ppp-radius ppp-winbind ppp-minconn ppp-password ppp-tools"
FILES_${PN}        = "/etc /usr/bin /usr/sbin/chat /usr/sbin/pppd"
FILES_${PN}-dbg += "${libdir}/pppd/2.4.3/.debug"
FILES_ppp-oa       = "/usr/lib/pppd/2.4.3/pppoatm.so"
FILES_ppp-oe       = "/usr/sbin/pppoe-discovery /usr/lib/pppd/2.4.3/rp-pppoe.so"
FILES_ppp-radius   = "/usr/lib/pppd/2.4.3/radius.so /usr/lib/pppd/2.4.3/radattr.so /usr/lib/pppd/2.4.3/radrealms.so"
FILES_ppp-winbind  = "/usr/lib/pppd/2.4.3/winbind.so"
FILES_ppp-minconn  = "/usr/lib/pppd/2.4.3/minconn.so"
FILES_ppp-password = "/usr/lib/pppd/2.4.3/pass*.so"
FILES_ppp-tools    = "/usr/sbin/pppstats /usr/sbin/pppdump"
DESCRIPTION_ppp-oa       = "Plugin for PPP needed for PPP-over-ATM"
DESCRIPTION_ppp-oe       = "Plugin for PPP needed for PPP-over-Ethernet"
DESCRIPTION_ppp-radius   = "Plugin for PPP that are related to RADIUS"
DESCRIPTION_ppp-winbind  = "Plugin for PPP to authenticate against Samba or Windows"
DESCRIPTION_ppp-minconn  = "Plugin for PPP to specify a minimum connect time before the idle timeout applies"
DESCRIPTION_ppp-password = "Plugin for PPP to get passwords via a pipe"
DESCRIPTION_ppp-tools    = "The pppdump and pppstats utitilities"
RDEPENDS_ppp_minconn    += "libpcap0.8"

pkg_postinst_${PN}() {
if test "x$D" != "x"; then
	exit 1
else
	chmod u+s ${sbindir}/pppd
fi
}
