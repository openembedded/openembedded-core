
SRC_URI = "svn://svn.opensync.org/plugins/syncml-client/plugin/tags;module=release-0.1;proto=http"
S = "${WORKDIR}/release-0.1"
PV = "0.1"

require libopensync-plugin.inc

DEPENDS += " syncml-client"

