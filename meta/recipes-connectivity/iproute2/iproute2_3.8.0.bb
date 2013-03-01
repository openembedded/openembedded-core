require iproute2.inc

PR = "r1"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch \
           file://0001-Fix-compilation-error-of-m_ipt.c-with-Werror-enabled.patch \
          "

SRC_URI[md5sum] = "951622fd770428116dc165acba375414"
SRC_URI[sha256sum] = "579145749f1aaf60e7c7a5de24b7f00fa2200a961094733c792b4ff139181e4f"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
