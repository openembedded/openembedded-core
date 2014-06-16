require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "dd6e1b4d9b9a28d127edb9f9b58bdec1"
SRC_URI[sha256sum] = "551df7a5f0c6e4d69523dd2b3aa2c54525b323457d5135f64816215bad3dc24c"

RRECOMMENDS_${PN} = "connman-conf"
