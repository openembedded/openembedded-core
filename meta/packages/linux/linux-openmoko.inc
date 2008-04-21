# extra stuff we need for openmoko that is not in linux.inc
do_deploy_append() {
	rm -f ${KERNEL_IMAGE_SYMLINK_NAME}.bin
	ln -sf ${DEPLOY_DIR_IMAGE}/uImage-${PV}-${PR}-${MACHINE_ARCH}.bin ${DEPLOY_DIR_IMAGE}/uImage-${MACHINE_ARCH}-latest.bin
}

RDEPENDS_kernel-image += "mtd-utils"

pkg_postinst_kernel-image () {
if test "x$D" != "x"; then
        exit 1
else
	if [ -f ${sysconfdir}/default/flashkernel ] ; then
		echo "Upgrading Kernel in Flash"
		echo "DO NOT stop this process"

		MTD_KERNEL_PARTITION=`cat /proc/mtd | grep kernel | cut -d':' -f1`
		MTD_KERNEL_PARTITION=/dev/$MTD_KERNEL_PARTITION

		if [ "x$MTD_KERNEL_PARTITION" = "x" ] ; then
			exit 1
		fi

		${bindir}/flash_eraseall $MTD_KERNEL_PARTITION
		${bindir}/nandwrite -p $MTD_KERNEL_PARTITION /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
	else
		touch ${sysconfdir}/default/flashkernel
	fi
fi
}

