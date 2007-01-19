DESCRIPTION="DBus-enabled dhcp client"
SECTION="net"
LICENSE="GPL"
HOMEPAGE="http://people.redhat.com/jvdias/dhcdbd/"

DEPENDS = "dbus"
RDEPENDS = "dhcp-client"

PR = "r0"

SRC_URI="http://people.redhat.com/dcantrel/dhcdbd/dhcdbd-${PV}.tar.bz2 \
         file://no-ext-options.patch;patch=1 \
         file://dhcdbd"

do_compile() {
	CC=${TARGET_SYS}-gcc DESTDIR=${prefix} make
}

do_install() {
	DESTDIR=${D} make install
	install -d ${D}/etc/init.d
	install -m 0755 ${WORKDIR}/dhcdbd ${D}/etc/init.d/
}

FILES_${PN} += "${sysconfdir} ${datadir}/dbus-1 ${base_sbindir}/*"
