AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-initramfs-${MACHINE}.cpio.gz"
APPEND += "root=/dev/ram0 "
TIMEOUT = "10"

EXCLUDE_FROM_WORLD = "1"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_bootimg[depends] += "poky-image-minimal-initramfs:do_rootfs"

inherit bootimg

do_bootimg_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}

