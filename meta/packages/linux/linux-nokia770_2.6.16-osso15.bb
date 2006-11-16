SECTION = "kernel"
DESCRIPTION = "Linux kernel for Nokia 770"
LICENSE = "GPL"
PR = "r4"

SRC_URI = "http://repository.maemo.org/pool/mistral/non-free/source/k/kernel-source-2.6.16/kernel-source-2.6.16_2.6.16.rel-osso15.tar.gz \
	   file://defconfig"

S = "${WORKDIR}/kernel-source-2.6.16-2.6.16.rel"

KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_HOST = "arm.*-linux"
COMPATIBLE_MACHINE = "nokia770|cmx270"

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
