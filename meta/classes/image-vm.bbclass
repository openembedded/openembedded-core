
SYSLINUX_ROOT ?= "root=/dev/sda2"
SYSLINUX_PROMPT ?= "0"
SYSLINUX_TIMEOUT ?= "10"
SYSLINUX_LABELS = "boot"
LABELS_append = " ${SYSLINUX_LABELS} "

# need to define the dependency and the ROOTFS for directdisk
do_bootdirectdisk[depends] += "${PN}:do_rootfs"
ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}-${MACHINE}.ext3"

# creating VM images relies on having a hddimg so ensure we inherit it here.
inherit boot-directdisk

IMAGE_TYPEDEP_vmdk = "ext3"
IMAGE_TYPEDEP_vdi = "ext3"
IMAGE_TYPES_MASKED += "vmdk vdi"

create_vmdk_image () {
    qemu-img convert -O vmdk ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vmdk
    ln -sf ${IMAGE_NAME}.vmdk ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.vmdk
}

create_vdi_image () {
    qemu-img convert -O vdi ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.hdddirect ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.vdi
    ln -sf ${IMAGE_NAME}.vdi ${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.vdi
}

python do_vmimg() {
    if 'vmdk' in d.getVar('IMAGE_FSTYPES', True):
        bb.build.exec_func('create_vmdk_image', d)
    if 'vdi' in d.getVar('IMAGE_FSTYPES', True):
        bb.build.exec_func('create_vdi_image', d)        
}

addtask vmimg after do_bootdirectdisk before do_build
do_vmimg[depends] += "qemu-native:do_populate_sysroot"

