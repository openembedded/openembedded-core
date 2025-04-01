require connman.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
           file://0001-plugin.h-Change-visibility-to-default-for-debug-symb.patch \
           file://0001-connman.service-stop-systemd-resolved-when-we-use-co.patch \
           file://connman \
           file://no-version-scripts.patch \
           file://0002-resolve-musl-does-not-implement-res_ninit.patch \
           "


SRC_URI[sha256sum] = "2be2b00321632b775f9eff713acd04ef21e31fbf388f6ebf45512ff4289574ff"

RRECOMMENDS:${PN} = "connman-conf"
RCONFLICTS:${PN} = "networkmanager"
