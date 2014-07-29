
#NOISO = "1"

SYSLINUX_ROOT ?= "root=/dev/sda2"
SYSLINUX_PROMPT ?= "0"
SYSLINUX_TIMEOUT ?= "10"
SYSLINUX_LABELS = "boot"
LABELS_append = " ${SYSLINUX_LABELS} "

# need to define the dependency and the ROOTFS for directdisk
do_bootdirectdisk[depends] += "${PN}:do_rootfs"
ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}-${MACHINE}.ext3"

# creating VMDK relies on having a live hddimg so ensure we
# inherit it here.
#inherit image-live
inherit boot-directdisk

IMAGE_TYPEDEP_vmdk = "ext3"
IMAGE_TYPES_MASKED += "vmdk"

create_vmdk_image () {
	qemu-img convert -O vmdk ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vmdk
	ln -sf ${IMAGE_NAME}.vmdk ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.vmdk
}

python do_vmdkimg() {
        bb.build.exec_func('create_vmdk_image', d)
}

#addtask vmdkimg after do_bootimg before do_build
addtask vmdkimg after do_bootdirectdisk before do_build

do_vmdkimg[depends] += "qemu-native:do_populate_sysroot" 

