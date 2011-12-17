require connman.inc

PR = "r6"

# 0.78 tag
SRCREV = "02f5d5fe2d7c71514a6387ba2b772b42d8e8d297"
SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git \
            file://add_xuser_dbus_permission.patch \
            file://ethernet_default.patch \
            file://disable_alg-test.patch \
            file://connman"
S = "${WORKDIR}/git"
