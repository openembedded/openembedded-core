require connman.inc

SRC_URI  = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
            file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
            file://0001-connman.service-stop-systemd-resolved-when-we-use-co.patch \
            file://connman \
            file://no-version-scripts.patch \
            file://includes.patch \
            "
SRC_URI_append_libc-musl = " file://0002-resolve-musl-does-not-implement-res_ninit.patch \
                             "

SRC_URI[md5sum] = "bae37b45ee9b3db5ec8115188f8a7652"
SRC_URI[sha256sum] = "66d7deb98371545c6e417239a9b3b3e3201c1529d08eedf40afbc859842cf2aa"

RRECOMMENDS_${PN} = "connman-conf"
