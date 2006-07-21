include dbus_${PV}.inc


DEPENDS = "expat glib-2.0 virtual/libintl dbus-native"
SRC_URI_EXTRA = "file://no-introspect.patch;patch=1 file://no-bindings.patch;patch=1"

FILES_${PN} += "${bindir}/dbus-daemon"
FILES_${PN}-dev += "${bindir}/dbus-binding-tool"

do_configure_prepend() {
	install -m 0644 ${STAGING_DIR}/${BUILD_SYS}/share/dbus/dbus-bus-introspect.xml ${S}/tools/
	install -m 0644 ${STAGING_DIR}/${BUILD_SYS}/share/dbus/dbus-glib-bindings.h ${S}/tools/
}
