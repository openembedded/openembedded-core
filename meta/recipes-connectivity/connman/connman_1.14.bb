require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://inet-fix-ip-cleanup-functions.patch \
            "

SRC_URI[md5sum] = "6c9ecaf9c044f8c66a7b465f6716b569"
SRC_URI[sha256sum] = "d0c37452a4e8eec27e433ff901a6c447963b9d8a8ceddd4a7311cc322e2dbf8c"

PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
