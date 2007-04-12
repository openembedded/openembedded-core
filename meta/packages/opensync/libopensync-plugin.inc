
PV = "0.22+svn${SRCDATE}"
PLUGIN_NAME ?= "${@bb.data.getVar('PN', d, 1).replace('libopensync-plugin-','')}"
PLUGIN_SONAME ?= "${@bb.data.getVar('PLUGIN_NAME', d, 1).replace('-','_')}.so"
SRC_URI ?= "svn://svn.opensync.org/plugins;module=${PLUGIN_NAME};proto=http"
S ?= "${WORKDIR}/${PLUGIN_NAME}"

LICENSE ?= "LGPL"
DEPENDS += " libopensync"
RRECOMMENDS += " msynctool"
HOMEPAGE ?= "http://www.opensync.org/"

inherit autotools pkgconfig

FILES_${PN} += "${datadir} ${libdir}/opensync/plugins/*.so"
FILES_${PN}-dbg += "${libdir}/opensync/plugins/.debug"

do_install() {
	install -d ${D}${datadir}/opensync/defaults
	install -d ${D}${libdir}/opensync/plugins
	install -m 644 src/${PLUGIN_NAME} ${D}${datadir}/opensync/defaults
	install -m 755 src/.libs/${PLUGIN_SONAME} ${D}${libdir}/opensync/plugins/
}

