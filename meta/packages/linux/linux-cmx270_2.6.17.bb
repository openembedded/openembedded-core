SECTION = "kernel"
DESCRIPTION = "Linux kernel CM-X270"
LICENSE = "GPL"
PR = "r7"

SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.17.tar.bz2 \
	   http://raph.people.8d.com/kernels/8d-cmx2xx-2.6.17.1-june21.diff;patch=1 \
           http://raph.people.8d.com/kernels/hardcode-archID.diff;patch=1 \
	   file://cm_x2xx_mbx.patch;patch=1                               \
           file://add_2700g_plat-r0.patch;patch=1                         \
	   file://mtd_fixes-r0.patch;patch=1                              \
	   file://mtd_fixes1-r0.patch;patch=1                             \
	   file://mach-types \
	   file://defconfig"

S = "${WORKDIR}//linux-2.6.17"

KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "cmx270"

do_configure_prepend() {
        install -m 0644 ${WORKDIR}/defconfig ${S}/.config
        install -m 0644 ${WORKDIR}/mach-types ${S}/arch/arm/tools/mach-types
        oe_runmake oldconfig
}

do_deploy() {
        install -d ${DEPLOY_DIR}/images
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR}/images/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
}

do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile

KERNEL_RELEASE = "2.6.17"