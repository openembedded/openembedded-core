PV = "0.22"
PLUGIN_ARCHIVE ?= "${@bb.data.getVar('PLUGIN_NAME', d, 1).replace('-sync','')}"
SRC_URI = "http://www.opensync.org/download/releases/0.22/libopensync-plugin-${PLUGIN_ARCHIVE}-${PV}.tar.bz2"

S = "${WORKDIR}/libopensync-plugin-${PLUGIN_ARCHIVE}-${PV}/"

