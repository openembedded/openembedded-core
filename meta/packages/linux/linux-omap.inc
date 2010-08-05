SECTION = "kernel"
DESCRIPTION = "Linux kernel for TI OMAP devices"
LICENSE = "GPL"

KERNEL_OUTPUT = "arch/${ARCH}/boot/compressed/${KERNEL_IMAGETYPE}"

inherit kernel deploy

COMPATIBLE_MACHINE = "omap-zoom"

do_configure_prepend() {

	rm -f ${S}/.config || true

        if [ "${TARGET_OS}" = "linux-gnueabi" -o  "${TARGET_OS}" = "linux-uclibcgnueabi" ]; then
                echo "CONFIG_AEABI=y"                   >> ${S}/.config
                echo "CONFIG_OABI_COMPAT=y"             >> ${S}/.config
        else
                echo "# CONFIG_AEABI is not set"        >> ${S}/.config
                echo "# CONFIG_OABI_COMPAT is not set"  >> ${S}/.config
        fi

        sed     -e '/CONFIG_AEABI/d' \
                -e '/CONFIG_OABI_COMPAT=/d' \
                '${WORKDIR}/defconfig-${MACHINE}' >>'${S}/.config'

        yes '' | oe_runmake oldconfig

}

do_deploy() {
	install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
	cd ${DEPLOYDIR}
	ln -sf ${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin ${KERNEL_IMAGETYPE}-${MACHINE}.bin
	tar -cvzf ${DEPLOYDIR}/modules-${KERNEL_RELEASE}-${MACHINE}.tgz -C ${D} lib
}

addtask deploy before do_populate_sysroot after do_install

