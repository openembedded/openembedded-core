require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "

SRC_URI[md5sum] = "47cce1d17a693dc307e6796c81991bd0"
SRC_URI[sha256sum] = "7e4e624cadf42feed18e783351b10f81ef23d7e298bddc7ffe26fe5e69f25b8b"

RRECOMMENDS_${PN} = "connman-conf"
