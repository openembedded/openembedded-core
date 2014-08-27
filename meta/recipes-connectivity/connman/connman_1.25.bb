require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "a449d2e49871494506e48765747e6624"
SRC_URI[sha256sum] = "c1d266d6be18d2f66231f3537a7ed17b57637ca43c27328bc13c508cbeacce6e"

RRECOMMENDS_${PN} = "connman-conf"
