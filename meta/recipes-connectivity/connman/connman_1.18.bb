require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "

SRC_URI[md5sum] = "7578ec8f0422d34f5a4ba51b605fe576"
SRC_URI[sha256sum] = "56c2ca7842be2100b6b59367845d5beec125f231af6ea505604992e0c5a69992"

PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
