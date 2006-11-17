SECTION = "kernel"
DESCRIPTION = "Linux kernel for Nokia 770"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://repository.maemo.org/pool/mistral/non-free/source/k/kernel-source-2.6.16/kernel-source-2.6.16_2.6.16.rel-osso15.tar.gz \
	   file://defconfig"

S = "${WORKDIR}/kernel-source-2.6.16-2.6.16.rel"

KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel

COMPATIBLE_MACHINE = "nokia770"

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/defconfig ${S}/.config

        if [ "${TARGET_OS}" == "linux-gnueabi" -o  "${TARGET_OS}" == "linux-uclibcgnueabi" ]; then
                echo "CONFIG_AEABI=y"                   >> ${S}/.config
                echo "CONFIG_OABI_COMPAT=y"             >> ${S}/.config
        else
                echo "# CONFIG_AEABI is not set"        >> ${S}/.config
                echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
        fi

        sed     -e '/CONFIG_AEABI/d' \
                -e '/CONFIG_OABI_COMPAT=/d' \
                '${WORKDIR}/defconfig' >>'${S}/.config'

        yes '' | oe_runmake oldconfig

}

do_deploy() {
        install -d ${DEPLOY_DIR}/images
        install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}
}

do_deploy[dirs] = "${S}"

addtask deploy before do_build after do_compile
