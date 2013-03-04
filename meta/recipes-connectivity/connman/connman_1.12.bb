require connman.inc

# 1.12 tag
SRCREV = "8397cbbc0a79d39cf3b1880f4fd3f3405b6bc6ad"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://inet-fix-ip-cleanup-functions.patch \
            file://add-in.h-for-ipv6.patch"
S = "${WORKDIR}/git"
PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
