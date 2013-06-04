require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://inet-fix-ip-cleanup-functions.patch \
            "

SRC_URI[md5sum] = "e785c0c4564bfffb5215272f1be6f17c"
SRC_URI[sha256sum] = "8229cc5e3e75197453ba2a644c03860a7cf9f4f27fcbf111bb589530d4efb58f"

PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
