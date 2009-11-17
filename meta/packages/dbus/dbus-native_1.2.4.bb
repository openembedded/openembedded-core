require dbus.inc
inherit native

DEPENDS = "glib-2.0-native libxml2-native expat-native"

PR = "r7"

EXTRA_OECONF_X = "--without-x"

do_install() {
	autotools_do_install

	# for dbus-glib-native introspection generation
	install -d ${D}${datadir}/dbus/
	install -m 0644 bus/session.conf ${D}${datadir}/dbus/session.conf
	
	# dbus-glib-native and dbus-glib need this xml file
	cd ${S}
	./bus/dbus-daemon --introspect > ${D}${datadir}/dbus/dbus-bus-introspect.xml
}
