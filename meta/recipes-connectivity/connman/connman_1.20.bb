require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "

SRC_URI[md5sum] = "15f5e0d89e01b81c79306f54a2014efd"
SRC_URI[sha256sum] = "4e638443be959913a5ce0d51df10018448ed08d06e4f8a47ce4b3c705bc78ac1"

RRECOMMENDS_${PN} = "connman-conf"
