
SRC_URI = "svn://svn.opensync.org/plugins;module=file-sync;proto=http"
S = "${WORKDIR}/file-sync"

require libopensync-plugin.inc
require libopensync-plugin-svn.inc

DEFAULT_PREFERENCE = "-1"
