inherit grub-efi-cfg
require conf/image-uefi.conf

efi_populate() {
	# DEST must be the root of the image so that EFIDIR is not
	# nested under a top level directory.
	DEST=$1

	install -d ${DEST}${EFIDIR}

	install -m 0644 ${DEPLOY_DIR_IMAGE}/grub-efi-${EFI_BOOT_IMAGE} ${DEST}${EFIDIR}/${EFI_BOOT_IMAGE}
	EFIPATH=$(echo "${EFIDIR}" | sed 's/\//\\/g')
	printf 'fs0:%s\%s\n' "$EFIPATH" "${EFI_BOOT_IMAGE}" >${DEST}/startup.nsh

	install -m 0644 ${GRUB_CFG} ${DEST}${EFIDIR}/grub.cfg
}

efi_iso_populate() {
	iso_dir=$1
	efi_populate $iso_dir
	# Build a EFI directory to create efi.img
	mkdir -p ${EFIIMGDIR}/${EFIDIR}
	cp $iso_dir/${EFIDIR}/* ${EFIIMGDIR}${EFIDIR}
	cp $iso_dir/${KERNEL_IMAGETYPE} ${EFIIMGDIR}
	EFIPATH=$(echo "${EFIDIR}" | sed 's/\//\\/g')
	printf 'fs0:%s\%s\n' "$EFIPATH" "grub-efi-${EFI_BOOT_IMAGE}" > ${EFIIMGDIR}/startup.nsh
	if [ -f "$iso_dir/initrd" ] ; then
		cp $iso_dir/initrd ${EFIIMGDIR}
	fi
}

efi_hddimg_populate() {
	efi_populate $1
}
