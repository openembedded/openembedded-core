require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "f835b8137ab198e0af5deab043e2890a"
SRC_URI[sha256sum] = "1dcac1d059d5303343e544fca0d0e76837cfb32f36e4a607a71c1b65ccf007c5"

RRECOMMENDS_${PN} = "connman-conf"
