# we dont need the kernel in the image
ROOTFS_POSTPROCESS_COMMAND += "rm -f ${IMAGE_ROOTFS}/boot/zImage*"

def wrt_get_kernel_version(d):
	import bb
	if bb.data.inherits_class('image_ipk', d):
		skd = bb.data.getVar('STAGING_KERNEL_DIR', d, 1)
		return base_read_file(skd+'/kernel-abiversion')
	return "-no kernel version for available-"
	
wrt_create_images() {
	I=${DEPLOY_DIR}/images
	KERNEL_VERSION="${@wrt_get_kernel_version(d)}"

	for type in ${IMAGE_FSTYPES}; do
		# generic
		trx -o ${I}/wrt-generic-${type}.trx ${I}/loader.gz \
		${I}/wrt-kernel-${KERNEL_VERSION}.lzma -a 0x10000 ${I}/${IMAGE_NAME}.rootfs.${type}
		
		# WRT54GS
		addpattern -2 -i ${I}/wrt-generic-${type}.trx -o ${I}/wrt54gs-${type}.trx -g
		
		# WRT54G
		sed "1s,^W54S,W54G," ${I}/wrt54gs-${type}.trx > ${I}/wrt54g-${type}.trx
		
		# motorola
		motorola-bin ${I}/wrt-generic-${type}.trx ${I}/motorola-${type}.bin
	done;
}

IMAGE_POSTPROCESS_COMMAND += "wrt_create_images;"

DEPENDS_prepend = "${@["wrt-imagetools-native ", ""][(bb.data.getVar('PACKAGES', d, 1) == '')]}"