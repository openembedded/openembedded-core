SECTION = "kernel"
DESCRIPTION = "Linux kernel for Nokia 770"
LICENSE = "GPL"
PR = "r4"

SRC_URI = "http://ewi546.ewi.utwente.nl/OE/source/kernel-source-2.6.12.3_2.6.12.3-osso14.tar.gz \
	   file://defconfig"

S = "${WORKDIR}/linux-2.6"

KERNEL_IMAGETYPE = "vmlinux"
KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "nokia770"

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
