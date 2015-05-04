require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://build-create-dirs-before-putting-files-in-them.patch \
            file://connman \
            "
SRC_URI[md5sum] = "5283884504860f5fba2e6f489f517293"
SRC_URI[sha256sum] = "2a5a69693566f7fd59b2e677fa89356ada6d709998aa665caef8707b1e7a8594"

RRECOMMENDS_${PN} = "connman-conf"

