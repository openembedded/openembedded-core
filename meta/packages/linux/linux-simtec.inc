require linux.inc

inherit deploy

do_prepdefconfig() {
	cp ${S}/arch/arm/configs/s3c24xx_simtec_defconfig ${WORKDIR}/defconfig
	if test -e ${WORKDIR}/${MACHINE}-defconfig-append; then
	   cat ${WORKDIR}/${MACHINE}-defconfig-append >> ${WORKDIR}/defconfig
	fi
}

addtask prepdefconfig before do_configure after do_patch

do_deploy() {
	install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
	cd ${DEPLOYDIR}
	ln -sf ${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin ${KERNEL_IMAGETYPE}-${MACHINE}.bin
	tar -cvzf ${DEPLOYDIR}/modules-${KERNEL_RELEASE}-${MACHINE}.tgz -C ${D} lib
}

addtask deploy before do_populate_sysroot after do_install
