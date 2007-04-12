
require libopensync-plugin.inc

DEPENDS += " libsyncml"

do_install() {
        install -d ${D}${datadir}/opensync/defaults
        install -d ${D}${libdir}/opensync/plugins
        install -m 644 src/syncml-http-server ${D}${datadir}/opensync/defaults
	install -m 644 src/syncml-obex-client ${D}${datadir}/opensync/defaults
        install -m 755 src/.libs/${PLUGIN_SONAME} ${D}${libdir}/opensync/plugins/
}

