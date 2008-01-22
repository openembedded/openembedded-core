DESCRIPTION = "Qemu helper scripts from Poky"
LICENSE = "GPL"
RDEPENDS = "qemu-sdk"
PR = "r5"

SRC_URI = "file://${OEROOT}/scripts/poky-qemu \
           file://${OEROOT}/scripts/poky-qemu-internal \
           file://${OEROOT}/scripts/poky-addptable2image \
           file://${OEROOT}/scripts/poky-qemu-ifup"
		      
S = "${WORKDIR}"
		      
PACKAGE_ARCH = "all"

inherit sdk
		      
do_install() {
	install -d ${D}${bindir}
	install -m 0755 poky-* ${D}${bindir}/
}  