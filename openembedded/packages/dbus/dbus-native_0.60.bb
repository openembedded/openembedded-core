include dbus_${PV}.inc

SRC_URI_EXTRA=""

inherit native

S = "${WORKDIR}/dbus-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/dbus"
DEPENDS = "glib-2.0-native"

PR = "r1"

do_stage() {
	oe_runmake install
	install -d ${STAGING_DATADIR}/dbus
	install -m 0644 tools/dbus-bus-introspect.xml ${STAGING_DATADIR}/dbus
	install -m 0644 tools/dbus-glib-bindings.h ${STAGING_DATADIR}/dbus
}

