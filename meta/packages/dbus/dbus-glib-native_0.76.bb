require dbus-glib.inc
inherit native

DEPENDS = "glib-2.0-native dbus-native"

PR = "r2"

SRC_URI += "file://run-with-tmp-session-bus.patch;patch=1"

do_stage() {
        autotools_stage_all
        install -d ${STAGING_DATADIR}/dbus
        install -m 0644 tools/dbus-glib-bindings.h ${STAGING_DATADIR}/dbus
}
