#
# Creates .srec files from images.
#
# Useful for loading with Yamon.

# Define SREC_VMAADDR in your machine.conf.

SREC_CMD = "${TARGET_PREFIX}objcopy -O srec -I binary --adjust-vma ${SREC_VMAADDR} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.${type} ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.${type}.srec"

# Do not build srec files for these types of images:
SREC_SKIP = "tar"

do_srec () {
    if [ ${SREC_VMAADDR} = "" ] ; then
       oefatal Cannot do_srec without SREC_VMAADDR defined.
    fi
    for type in ${IMAGE_FSTYPES}; do
        for skiptype in ${SREC_SKIP}; do
            if [ $type = $skiptype ] ; then continue 2 ; fi
        done
        ${SREC_CMD}
    done
    return 0
}

addtask srec after do_rootfs before do_build
