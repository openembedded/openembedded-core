require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "
SRC_URI[md5sum] = "ba05b110b7c81e5fa14e8b402ef37a9e"
SRC_URI[sha256sum] = "7184e4b6d954449ee00a30e188924b3e37a20ad2fd9a0b76a2bdd82c863dcf8a"

RRECOMMENDS_${PN} = "connman-conf"
