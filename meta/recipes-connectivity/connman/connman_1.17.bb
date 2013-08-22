require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "

SRC_URI[md5sum] = "dd4a13f789de1b69fcddf0cf613f2d5b"
SRC_URI[sha256sum] = "d31aa2e7dc9fa817c93aba973995b63506a8c83f55afe507028f09b580ef0b00"

PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
