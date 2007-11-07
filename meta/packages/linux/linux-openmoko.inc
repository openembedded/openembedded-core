# extra stuff we need for openmoko that is not in linux.inc

do_deploy_append() {
	mv ${DEPLOY_DIR_IMAGE}/modules-${KERNEL_RELEASE}-${PR}-${MACHINE}.tgz ${DEPLOY_DIR_IMAGE}/modules-${PV}-${PR}-${MACHINE_CLASS}.tgz
	mv ${DEPLOY_DIR_IMAGE}/uImage-${PV}-${PR}-${MACHINE}-${DATETIME}.bin ${DEPLOY_DIR_IMAGE}/uImage-${PV}-${PR}-${MACHINE_CLASS}.bin
	rm -f ${KERNEL_IMAGE_SYMLINK_NAME}.bin
	ln -sf ${DEPLOY_DIR_IMAGE}/uImage-${PV}-${PR}-${MACHINE_CLASS}.bin ${DEPLOY_DIR_IMAGE}/uImage-${MACHINE_CLASS}-latest.bin
}

