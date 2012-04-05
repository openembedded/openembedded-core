require connman.inc

# 0.79 tag
SRCREV = "442b1fe603e005814f592a3dbcf0d0bfb13f961c"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://add_xuser_dbus_permission.patch \
            file://ethernet_default.patch \
            file://disable_alg-test.patch \
            file://connman"
S = "${WORKDIR}/git"
PR = "r1"
