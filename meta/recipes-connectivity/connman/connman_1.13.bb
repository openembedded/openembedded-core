require connman.inc

# 1.13 tag
SRCREV = "96fcd8871531c9012135110769618d65a3523b4d"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://inet-fix-ip-cleanup-functions.patch \
            "
S = "${WORKDIR}/git"
PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
