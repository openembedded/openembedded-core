PV = "0.22"
PLUGIN_ARCHIVE ?= "${@bb.data.getVar('PLUGIN_NAME', d, 1).replace('-sync','')}"
SRC_URI = "http://www.opensync.org/attachment/wiki/download/libopensync-plugin-${PLUGIN_ARCHIVE}-${PV}.tar.bz2?format=raw"

S = "${WORKDIR}/libopensync-plugin-${PLUGIN_ARCHIVE}-${PV}/"

do_unpack2() {
    cd ${WORKDIR} && tar jxf libopensync-plugin-${PLUGIN_ARCHIVE}-${PV}.tar.bz2?format=raw
}

addtask unpack2 after do_unpack before do_patch

