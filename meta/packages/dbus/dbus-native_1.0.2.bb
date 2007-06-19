DESCRIPTION = "message bus system for applications to talk to one another"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "GPL"
SECTION = "base"

PR = "r0"

DEPENDS = "glib-2.0-native libxml2-native expat-native"

DEFAULT_PREFERENCE = "-1"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/dbus-1.0.2"
SRC_URI = "http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz \
	   file://cross.patch;patch=1 \
	   "

inherit autotools pkgconfig gettext native

S = "${WORKDIR}/dbus-${PV}"

EXTRA_OECONF = " --disable-tests --disable-checks --disable-xml-docs \
                 --disable-doxygen-docs --with-xml=expat --without-x"

do_stage () {
	oe_runmake install
	autotools_stage_all

	# for dbus-glib-native introspection generation
	install -d ${STAGING_DATADIR}/dbus
	install -m 0644 bus/session.conf ${STAGING_DATADIR}/dbus/session.conf
}
