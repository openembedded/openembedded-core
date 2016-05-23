require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
           file://iproute2-4.3.0-musl.patch \
          "
SRC_URI[md5sum] = "d015e437e4f744d51d3a1a53341826d5"
SRC_URI[sha256sum] = "74fc6a8ad085be095a159f8158bbaf385b42af9e101619f233f1ae466829d406"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
