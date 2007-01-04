
SRC_URI = "svn://svn.opensync.org/plugins;module=evolution2;proto=http"
S = "${WORKDIR}/evolution2"

require libopensync-plugin.inc

DEPENDS += " eds-dbus"

