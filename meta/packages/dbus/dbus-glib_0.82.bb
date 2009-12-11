require dbus-glib.inc

PR = "r0"

do_configure_prepend() {
	cp -p ${STAGING_DATADIR_NATIVE}/dbus/dbus-glib-bindings.h ${S}/tools/
}

EXTRA_OECONF += "--with-dbus-binding-tool=${STAGING_BINDIR_NATIVE}/dbus-binding-tool"
