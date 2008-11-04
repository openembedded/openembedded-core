require linux.inc

do_prepdefconfig() {
	cp ${S}/arch/arm/configs/s3c24xx_simtec_defconfig ${WORKDIR}/defconfig
	if test -e ${WORKDIR}/${MACHINE}-defconfig-append; then
	   cat ${WORKDIR}/${MACHINE}-defconfig-append >> ${WORKDIR}/defconfig
	fi
}

addtask prepdefconfig before do_configure after do_patch

do_deploy() {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 arch/${ARCH}/boot/${KERNEL_IMAGETYPE} ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin
	cd ${DEPLOY_DIR_IMAGE}
	ln -sf ${KERNEL_IMAGETYPE}-${PV}-${MACHINE}-${DATETIME}.bin ${KERNEL_IMAGETYPE}-${MACHINE}.bin
	tar -cvzf ${DEPLOY_DIR_IMAGE}/modules-${KERNEL_RELEASE}-${MACHINE}.tgz -C ${D} lib	
}

do_deploy[dirs] = "${S}"

addtask deploy before do_populate_staging after do_install
