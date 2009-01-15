require dbus-glib.inc

PR = "r0"

do_configure_prepend() {
	cp -p ${STAGING_DATADIR_NATIVE}/dbus/dbus-glib-bindings.h ${S}/tools/
}

SRC_URI += "file://native-binding-tool.patch;patch=1"
