require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://connman \
            "
SRC_URI_append_libc-musl = "file://0002-resolve-musl-does-not-implement-res_ninit.patch \
                            file://0003-Fix-header-inclusions-for-musl.patch \
                           "

SRC_URI[md5sum] = "d0c3071c1d8dec9cd17b760f862de2ad"
SRC_URI[sha256sum] = "3185864c73206a6033d12e9f583689dcd03f714a40a58333709d3f74a4e0934c"

RRECOMMENDS_${PN} = "connman-conf"

