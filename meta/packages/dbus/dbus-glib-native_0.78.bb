require dbus-glib.inc
inherit native

DEPENDS = "glib-2.0-native dbus-native"

PR = "r0"

SRC_URI += "file://run-with-tmp-session-bus.patch;patch=1"

do_install_append() {
        install -d ${D}${datadir}/dbus
        install -m 0644 tools/dbus-glib-bindings.h ${D}${datadir}/dbus
}
