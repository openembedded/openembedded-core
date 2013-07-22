require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            "

SRC_URI[md5sum] = "d2906816222219e10426442d07c3f4c1"
SRC_URI[sha256sum] = "7273e88e6a6338be1e51b0e4c685d556386897cba9317cd83370bfb3f009982a"

PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
