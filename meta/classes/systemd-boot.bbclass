# Copyright (C) 2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# systemd-boot.bbclass - The "systemd-boot" is essentially the gummiboot merged into systemd.
#                        The original standalone gummiboot project is dead without any more
#                        maintenance.
#
# Set EFI_PROVIDER = "systemd-boot" to use systemd-boot on your live images instead of grub-efi
# (images built by image-live.bbclass)

do_bootimg[depends] += "${MLPREFIX}systemd-boot:do_deploy"

require conf/image-uefi.conf
# Need UUID utility code.
inherit fs-uuid

efi_populate() {
        DEST=$1

        install -d ${DEST}${EFIDIR}
        # systemd-boot requires these paths for configuration files
        # they are not customizable so no point in new vars
        install -d ${DEST}/loader
        install -d ${DEST}/loader/entries
        install -m 0644 ${DEPLOY_DIR_IMAGE}/systemd-${EFI_BOOT_IMAGE} ${DEST}${EFIDIR}/${EFI_BOOT_IMAGE}
        EFIPATH=$(echo "${EFIDIR}" | sed 's/\//\\/g')
        printf 'fs0:%s\%s\n' "$EFIPATH" "${EFI_BOOT_IMAGE}" >${DEST}/startup.nsh
        install -m 0644 ${SYSTEMD_BOOT_CFG} ${DEST}/loader/loader.conf
        for i in ${SYSTEMD_BOOT_ENTRIES}; do
            install -m 0644 ${i} ${DEST}/loader/entries
        done
}

efi_iso_populate() {
        iso_dir=$1
        efi_populate $iso_dir
        mkdir -p ${EFIIMGDIR}/${EFIDIR}
        cp $iso_dir/${EFIDIR}/* ${EFIIMGDIR}${EFIDIR}
        cp -r $iso_dir/loader ${EFIIMGDIR}
        cp $iso_dir/${KERNEL_IMAGETYPE} ${EFIIMGDIR}
        EFIPATH=$(echo "${EFIDIR}" | sed 's/\//\\/g')
        echo "fs0:${EFIPATH}\\${EFI_BOOT_IMAGE}" > ${EFIIMGDIR}/startup.nsh
        if [ -f "$iso_dir/initrd" ] ; then
            cp $iso_dir/initrd ${EFIIMGDIR}
        fi
}

efi_hddimg_populate() {
        efi_populate $1
}

inherit systemd-boot-cfg
