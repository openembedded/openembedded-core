require connman.inc

# 1.4 tag
SRCREV = "f701bbca259f1f35e68d338f31f5373f75f3da5f"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://add_xuser_dbus_permission.patch \
            file://connman \
            file://0002-storage.c-If-there-is-no-d_type-support-use-fstatat.patch \
            file://0001-timezone.c-If-there-is-no-d_type-support-use-fstatat.patch"
S = "${WORKDIR}/git"
PR = "${INC_PR}.0"
