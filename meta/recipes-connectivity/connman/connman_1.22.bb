require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "4f45ab2035d1028a287d14427ce61774"
SRC_URI[sha256sum] = "64d9a8ab83c99943514bb64984142fef409177a93c64e1aace84eaf13c7bddde"

RRECOMMENDS_${PN} = "connman-conf"
