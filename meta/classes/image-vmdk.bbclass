
NOISO = "1"
SYSLINUX_PROMPT = "0"
SYSLINUX_TIMEOUT = "1"
SYSLINUX_LABELS = "boot"

# creating VMDK relies on having a live hddimg so ensure we
# inherit it here.
inherit image-live

create_vmdk_image () {
	qemu-img convert -O vmdk ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hddimg ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vmdk
}

python do_vmdkimg() {
        bb.build.exec_func('create_vmdk_image', d)
}

addtask vmdkimg after do_bootimg before do_build
do_vmdkimg[nostamp] = "1"

do_vmdkimg[depends] += "qemu-native:do_populate_sysroot" 

