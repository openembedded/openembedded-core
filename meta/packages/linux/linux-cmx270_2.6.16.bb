SECTION = "kernel"
DESCRIPTION = "Linux kernel CM-X270"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.16.tar.bz2 \
	   file://diff-2.6.16-cm-x270;patch=1                               \
	   file://CL_FlashDrv"

S = "${WORKDIR}//linux-2.6.16"

KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "cmx270"

do_configure_prepend() {
        install -m 0644 ${S}/arch/arm/configs/cm_x270_defconfig ${S}/.config
        install -m 0644 ${WORKDIR}/CL_FlashDrv ${S}/drivers/block/cl_flash
        oe_runmake oldconfig
}

do_deploy() {
        install -d ${DEPLOY_DIR}/images
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR}/images/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
}

do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile

KERNEL_RELEASE = "2.6.16"