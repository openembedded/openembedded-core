require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "483633162b819c8b99fec970f92e311d"
SRC_URI[sha256sum] = "e80f4e9c639ef016e9c497c122c349dbc9e6dc78a9976f986134739ee208c08a"

RRECOMMENDS_${PN} = "connman-conf"
