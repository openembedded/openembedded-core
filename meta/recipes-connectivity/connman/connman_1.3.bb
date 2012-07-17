require connman.inc

# 1.3 tag
SRCREV = "3c0fa84091524c7cd6237744f2088ffee2f1d5ad"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://0002-storage.c-If-there-is-no-d_type-support-use-fstatat.patch \
            file://0001-timezone.c-If-there-is-no-d_type-support-use-fstatat.patch"
S = "${WORKDIR}/git"
PR = "${INC_PR}.1"
