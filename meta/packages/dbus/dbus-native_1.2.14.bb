require dbus.inc
inherit native

DEPENDS = "glib-2.0-native libxml2-native expat-native"

PR = "r3"

do_stage() {
	oe_runmake install
	autotools_stage_all

	# for dbus-glib-native introspection generation
	install -d ${STAGING_DATADIR}/dbus
	install -m 0644 bus/session.conf ${STAGING_DATADIR}/dbus/session.conf
	
	# dbus-glib-native and dbus-glib need this xml file
	install -d ${STAGING_DATADIR_NATIVE}/dbus/
	cd ${S}
	./bus/dbus-daemon --introspect > ${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml
}

do_install() {
	:
}
