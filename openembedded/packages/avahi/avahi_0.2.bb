DESCRIPTION = "Avahi implements the DNS-SD over Multicast DNS"
HOMEPAGE = "http://www.freedesktop.org/Software/Avahi"
LICENCE= "GPL"
DEPENDS = "expat libdaemon dbus-0.34"
RRECOMMENDS = "libnss-mdns"
SECTION = "net"
PRIORITY = "optional"

SRC_URI = "http://www.freedesktop.org/~lennart/avahi-${PV}.tar.gz"

#	   file://no-strict-ansi.patch;patch=1"

PACKAGES = "avahi-daemon libavahi-common libavahi-core libavahi-client avahi-dnsconfd libavahi-glib avahi-dev avahi-doc"

FILES_libavahi-common = "${libdir}/libavahi-common.so.*"
FILES_libavahi-core= "${libdir}/libavahi-core.so.*"
FILES_avahi-daemon = "${sbindir}/avahi-daemon \
		      ${sysconfdir}/avahi/avahi-daemon.conf \
		      ${sysconfdir}/avahi/services \
		      ${sysconfdir}/dbus-1 \
		      ${sysconfdir}/init.d/avahi-daemon \
		      ${datadir}/avahi/introspection/*.introspect \
		      ${datadir}/avahi/avahi-service.dtd"
FILES_libavahi-client = "${libdir}/libavahi-client.so.*"
FILES_avahi-dnsconfd = "${sbindir}/avahi-dnsconfd \
			${sysconfdir}/avahi/avahi-dnsconfd.action \
			${sysconfdir}/init.d/avahi-dnsconfd"
FILES_libavahi-glib = "${libdir}/libavahi-glib.so.*"

CONFFILES_avahi-daemon = "${sysconfdir}/avahi/avahi-daemon.conf"

EXTRA_OECONF = "--with-distro=debian --disable-gtk --disable-python"
inherit autotools
inherit update-rc.d

INITSCRIPT_PACKAGES = "avahi-daemon avahi-dnsconfd"
INITSCRIPT_NAME_avahi-daemon = "avahi-daemon"
INITSCRIPT_NAME_avahi-dnsconfd = "avahi-dnsconfd"

pkg_postinst_avahi-daemon () {
	grep avahi /etc/passwd || adduser --disabled-password --system --home /var/run/avahi-daemon avahi
}

pkg_postrm_avahi-daemon () {
	deluser avahi || true
}

