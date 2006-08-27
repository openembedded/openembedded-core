DESCRIPTION = "Avahi implements the DNS-SD over Multicast DNS"
SECTION = "network"
PRIORITY = "optional"
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://avahi.org"
MAINTAINER = "Philipp Zabel <philipp.zabel@gmail.com>"
LICENSE= "GPL"
PR = "r1"

DEPENDS = "expat libdaemon dbus"
RRECOMMENDS = "libnss-mdns"

SRC_URI = "http://avahi.org/download/avahi-${PV}.tar.gz"

PACKAGES =+ "avahi-daemon libavahi-common libavahi-core libavahi-client avahi-dnsconfd libavahi-glib avahi-dev avahi-doc avahi-utils"

FILES_libavahi-common = "${libdir}/libavahi-common.so.*"
FILES_libavahi-core= "${libdir}/libavahi-core.so.*"
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
FILES_avahi-utils = "${bindir}/avahi-*"

CONFFILES_avahi-daemon = "${sysconfdir}/avahi/avahi-daemon.conf"

EXTRA_OECONF = "--with-distro=debian --disable-gdbm --disable-gtk --disable-mono --disable-monodoc --disable-qt3 --disable-qt4 --disable-python"
inherit autotools pkgconfig update-rc.d


do_stage() {
	autotools_stage_all
}

INITSCRIPT_PACKAGES = "avahi-daemon avahi-dnsconfd"
INITSCRIPT_NAME_avahi-daemon = "avahi-daemon"
INITSCRIPT_PARAMS_avahi-daemon = "defaults 21 19"
INITSCRIPT_NAME_avahi-dnsconfd = "avahi-dnsconfd"
INITSCRIPT_PARAMS_avahi-dnsconfd = "defaults 22 19"

# At the time the postinst runs, dbus might not be setup so only restart if running

pkg_postinst_avahi-daemon () {
	# can't do this offline
	if [ "x$D" != "x" ]; then
		exit 1
	fi
	grep avahi /etc/group || addgroup avahi
	grep avahi /etc/passwd || adduser --disabled-password --system --home /var/run/avahi-daemon --no-create-home avahi --ingroup avahi -g Avahi

	DBUSPID=`pidof dbus-daemon`

	if [ "x$DBUSPID" != "x" ]; then
		/etc/init.d/dbus-1 force-reload
	fi
}

pkg_postrm_avahi-daemon () {
	deluser avahi || true
	delgroup avahi || true
}
