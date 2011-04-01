SUMMARY = "Avahi IPv4LL network address configuration daemon"
DESCRIPTION = "Avahi is a fully LGPL framework for Multicast DNS Service Discovery. It \
allows programs to publish and discover services and hosts running on a local network \
with no specific configuration. This tool implements IPv4LL, "Dynamic Configuration of \
IPv4 Link-Local Addresses" (IETF RFC3927), a protocol for automatic IP address \
configuration from the link-local 169.254.0.0/16 range without the need for a central \
server."
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://avahi.org"
BUGTRACKER = "http://avahi.org/report"
SECTION = "network"
PRIORITY = "optional"

# major part is under LGPLv2.1+, but several .dtd, .xsl, initscripts and
# python scripts are under GPLv2+
LICENSE = "GPLv2+ & LGPLv2.1+"

DEPENDS = "expat libcap libdaemon dbus glib-2.0 gtk+ libglade"

# uclibc has no nss
RRECOMMENDS_avahi-daemon_append_libc-glibc = "libnss-mdns"
RRECOMMENDS_${PN}_append_libc-glibc = "libnss-mdns"

SRC_URI = "http://avahi.org/download/avahi-${PV}.tar.gz \
        file://00avahi-autoipd \
        file://99avahi-autoipd \
        file://initscript.patch;patch=1"

inherit autotools pkgconfig update-rc.d gettext

EXTRA_OECONF = "--with-distro=debian \
             --with-avahi-priv-access-group=adm \
             --disable-stack-protector \
             --disable-gdbm \
             --disable-mono \
             --disable-monodoc \
             --disable-qt3 \
             --disable-qt4 \
             --disable-python \
             --disable-doxygen-doc"

PACKAGES =+ "avahi-daemon libavahi-common libavahi-core libavahi-client avahi-dnsconfd libavahi-glib libavahi-gobject avahi-autoipd avahi-ui-utils avahi-utils libavahi-ui libavahi-ui-dev"

FILES_libavahi-common = "${libdir}/libavahi-common.so.*"
FILES_libavahi-core = "${libdir}/libavahi-core.so.*"
FILES_libavahi-ui = "${libdir}/libavahi-ui.so.*"
FILES_libavahi-ui-dev = "${libdir}/libavahi-ui.* \
                         ${includedir}/avahi-ui/* \
                         ${libdir}/pkgconfig/avahi-ui.pc"
FILES_avahi-daemon = "${sbindir}/avahi-daemon \
                      ${sysconfdir}/avahi/avahi-daemon.conf \
                      ${sysconfdir}/avahi/hosts \
                      ${sysconfdir}/avahi/services \
                      ${sysconfdir}/dbus-1 \
                      ${sysconfdir}/init.d/avahi-daemon \
                      ${datadir}/avahi/introspection/*.introspect \
                      ${datadir}/avahi/avahi-service.dtd \
                      ${datadir}/avahi/service-types"
FILES_libavahi-client = "${libdir}/libavahi-client.so.*"
FILES_avahi-dnsconfd = "${sbindir}/avahi-dnsconfd \
                        ${sysconfdir}/avahi/avahi-dnsconfd.action \
                        ${sysconfdir}/init.d/avahi-dnsconfd"
FILES_libavahi-glib = "${libdir}/libavahi-glib.so.*"
FILES_libavahi-gobject = "${libdir}/libavahi-gobject.so.*"
FILES_avahi-utils = "${bindir}/avahi-*"
FILES_avahi-ui-utils = "${bindir}/bssh ${bindir}/bvnc ${bindir}/bshell \
                        ${bindir}/avahi-discover-standalone \
                        ${datadir}/applications \
                        ${datadir}/avahi/interfaces"

RRECOMMENDS_avahi-dev = "expat-dev libcap-dev libdaemon-dev dbus-dev glib-2.0-dev update-rc.d-dev"
RRECOMMENDS_avahi-dev_append_libc-glibc = " gettext-dev"

RRECOMMENDS_avahi-dev[nodeprrecs] = 1

CONFFILES_avahi-daemon = "${sysconfdir}/avahi/avahi-daemon.conf"

INITSCRIPT_PACKAGES = "avahi-daemon avahi-dnsconfd"
INITSCRIPT_NAME_avahi-daemon = "avahi-daemon"
INITSCRIPT_PARAMS_avahi-daemon = "defaults 21 19"
INITSCRIPT_NAME_avahi-dnsconfd = "avahi-dnsconfd"
INITSCRIPT_PARAMS_avahi-dnsconfd = "defaults 22 19"

do_install() {
	autotools_do_install

	# don't install /var/run when populating rootfs. Do it through volatile
	# /var/run of current version is empty, so just remove it.
	# if /var/run become non-empty in the future, need to install it via volatile
	rm -rf ${D}/var/run
}

# At the time the postinst runs, dbus might not be setup so only restart if running

pkg_postinst_avahi-daemon () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi
	grep "^avahi:" /etc/group > /dev/null || addgroup avahi
	grep "^avahi:" /etc/passwd > /dev/null || adduser --disabled-password --system --home /var/run/avahi-daemon --no-create-home avahi --ingroup avahi -g Avahi

	DBUSPID=`pidof dbus-daemon`

	if [ "x$DBUSPID" != "x" ]; then
		/etc/init.d/dbus-1 force-reload
	fi
}

pkg_postinst_avahi-autoipd () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi
	grep "^avahi-autoipd:" /etc/group > /dev/null || addgroup avahi-autoipd
	grep "^avahi-autoipd:" /etc/passwd > /dev/null || adduser --disabled-password --system --home /var/lib/avahi-autoipd --no-create-home avahi-autoipd --ingroup avahi-autoipd -g "Avahi autoip daemon"
}

pkg_postrm_avahi-daemon () {
	deluser avahi || true
	delgroup avahi || true
}

pkg_postrm_avahi-autoipd () {
	deluser avahi-autoipd || true
	delgroup avahi-autoipd || true
}

do_install_avahi-autoipd() {
	autotools_do_install
	install -d ${D}${sysconfdir}/udhcpc.d
	install ${WORKDIR}/00avahi-autoipd ${D}${sysconfdir}/udhcpc.d
	install ${WORKDIR}/99avahi-autoipd ${D}${sysconfdir}/udhcpc.d
}
