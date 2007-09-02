EXTRA_IMAGEDEPENDS += "sharp-flash-header-c700"

IMAGE_CMD_jffs2 = "mkfs.jffs2 --root=${IMAGE_ROOTFS} --faketime -m favourlzo \
                   --output=${T}/${IMAGE_NAME}.rootfs.jffs2 --pad --little-endian --eraseblock=${ERASEBLOCKSIZE} -n \
                   && sumtool -i ${T}/${IMAGE_NAME}.rootfs.jffs2 \
                   -o ${T}/${IMAGE_NAME}.rootfs.jffs2.summary \
                   --eraseblock=${ERASEBLOCKSIZE} -l -p; \
                   cat ${STAGING_LIBDIR}/sharp-flash-header/header-c700.bin \
                   ${T}/${IMAGE_NAME}.rootfs.jffs2 > ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2; \
                   rm ${T}/${IMAGE_NAME}.rootfs.jffs2; \
                   cat ${STAGING_LIBDIR}/sharp-flash-header/header-c700.bin \
                   ${T}/${IMAGE_NAME}.rootfs.jffs2.summary > ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.summary.jffs2; \
                   rm ${T}/${IMAGE_NAME}.rootfs.jffs2.summary;"
