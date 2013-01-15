require connman.inc

# 1.10 tag
SRCREV = "3bda76eb442c9395db1dba17352c3e78376671cc"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://0002-storage.c-If-there-is-no-d_type-support-use-fstatat.patch \
            file://inet-fix-ip-cleanup-functions.patch"
S = "${WORKDIR}/git"
PR = "${INC_PR}.0"

RRECOMMENDS_${PN} = "connman-conf"
