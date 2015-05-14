python __anonymous () {
    kerneltype = d.getVar('KERNEL_IMAGETYPE', True)
    if kerneltype == 'uImage':
        depends = d.getVar("DEPENDS", True)
        depends = "%s u-boot-mkimage-native" % depends
        d.setVar("DEPENDS", depends)
}

uboot_prep_kimage() {
	if test -e arch/${ARCH}/boot/compressed/vmlinux ; then
		vmlinux_path="arch/${ARCH}/boot/compressed/vmlinux"
		linux_suffix=""
		linux_comp="none"
	else
		vmlinux_path="vmlinux"
		linux_suffix=".gz"
		linux_comp="gzip"
	fi

	${OBJCOPY} -O binary -R .note -R .comment -S "${vmlinux_path}" linux.bin

	if [ "${linux_comp}" != "none" ] ; then
		rm -f linux.bin
		gzip -9 linux.bin
		mv -f "linux.bin${linux_suffix}" linux.bin
	fi

	echo "${linux_comp}"
}

do_uboot_mkimage() {
	if test "x${KERNEL_IMAGETYPE}" = "xuImage" ; then
		if test "x${KEEPUIMAGE}" != "xyes" ; then
			uboot_prep_kimage

			ENTRYPOINT=${UBOOT_ENTRYPOINT}
			if test -n "${UBOOT_ENTRYSYMBOL}"; then
				ENTRYPOINT=`${HOST_PREFIX}nm ${S}/vmlinux | \
					awk '$3=="${UBOOT_ENTRYSYMBOL}" {print $1}'`
			fi

			uboot-mkimage -A ${UBOOT_ARCH} -O linux -T kernel -C "${linux_comp}" -a ${UBOOT_LOADADDRESS} -e $ENTRYPOINT -n "${DISTRO_NAME}/${PV}/${MACHINE}" -d linux.bin arch/${ARCH}/boot/uImage
			rm -f linux.bin
		fi
	fi
}

addtask uboot_mkimage before do_install after do_compile
