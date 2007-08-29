# Zaurus Clamshell specific configuration for kernel 2.6
# Don't forget to include zaurus-clamshell.inc as well

PREFERRED_PROVIDER_virtual/kernel = "linux-rp"
PREFERRED_VERSION_wpa_supplicant = "0.4.7"

PCMCIA_MANAGER ?= "pcmciautils"

MACHINE_FEATURES = "kernel26 apm alsa pcmcia irda usbgadget keyboard touchscreen bluetooth"
MACHINE_FEATURES_append_tosa  = " usbhost usbwlanng "
MACHINE_FEATURES_append_akita = " usbhost "
MACHINE_FEATURES_append_spitz = " usbhost ext2 "

MACHINE_EXTRA_RDEPENDS = "zaurusd"
MACHINE_EXTRA_RRECOMMENDS_c7x0 = "kernel-module-snd-soc-corgi"
MACHINE_EXTRA_RRECOMMENDS_akita = "kernel-module-snd-soc-spitz"
MACHINE_EXTRA_RRECOMMENDS_spitz = "kernel-module-snd-soc-spitz"
MACHINE_EXTRA_RRECOMMENDS_poodle = "kernel-module-snd-soc-poodle"

MACHINE_POSTPROCESS_COMMAND = "zaurus_make_installkit"

zaurus_make_installkit () {
    cd ${DEPLOY_DIR_IMAGE}
    rm -rf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-installkit/         
    mkdir ${IMAGE_NAME}-installkit/

    # Get the latest kernel using the zImage-<machine>.bin symlink
    cp zImage-${MACHINE}.bin ${IMAGE_NAME}-installkit/zImage.bin

    if [ "${MACHINE}" = "spitz" ]; then
        cp gnu-tar ${IMAGE_NAME}-installkit/
        cp ${IMAGE_NAME}.rootfs.tar.gz ${IMAGE_NAME}-installkit/hdimage1.tgz
    else
        cp ${IMAGE_NAME}.rootfs.jffs2 ${IMAGE_NAME}-installkit/initrd.bin
    fi

    # All zaurus machines except collie need updater.sh
    if [ "${MACHINE}" != "collie" ]; then
        cp updater.sh.${MACHINE} ${IMAGE_NAME}-installkit/updater.sh
    fi
 
    tar czf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-installkit.tgz ${IMAGE_NAME}-installkit/
    md5sum ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-installkit.tgz > ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-installkit.tgz.md5
    rm -rf ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}-installkit/     
}

# Don't include kernels in standard images for Zaurus machines
RDEPENDS_kernel-base = ""
KERNEL_IMAGETYPE = "zImage"
