DESCRIPTION = "2.6 Linux Kernel for IGEP based platforms"
SECTION = "kernel"
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7 "

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_igep0020 = "1"
DEFAULT_PREFERENCE_igep0030 = "1"

COMPATIBLE_MACHINE = "(igep0020|igep0030)"

inherit kernel

KV = "${PV}-0"

SRC_URI = "http://downloads.igep.es/sources/linux-omap-${KV}.tar.gz \
           file://0001-omap3-init-MUX-for-OMAP3-IGEP-module.patch;patch=1 \
           file://defconfig-igep0020 \
           file://defconfig-igep0030"

do_configure() {

	rm -f ${S}/.config || true

   	cp ${WORKDIR}/defconfig-${MACHINE} ${S}/.config

        yes '' | oe_runmake oldconfig

}
           
S = "${WORKDIR}/linux-omap-${KV}"
