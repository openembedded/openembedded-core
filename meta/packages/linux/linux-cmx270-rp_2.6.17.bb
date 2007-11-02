SECTION = "kernel"
DESCRIPTION = "Linux kernel for Compulab cmx270"
LICENSE = "GPL"
PR = "r1"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.17.tar.bz2 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_ide-r1.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_mtd-r2.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/it8152_pci-r1.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_core-r2.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_mbx.patch;patch=1 \ 
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_pccard-r1.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/it8711_sio-r1.patch;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/cmx270/cm_x2xx_core_Kconfig-r1.patch;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-2.6.17"

# to get module dependencies working
KERNEL_RELEASE = "2.6.17"

KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "cmx270"

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config
        oe_runmake oldconfig
}

do_deploy() {
        install -d ${DEPLOY_DIR}/images
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR}/images/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
}

do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile
