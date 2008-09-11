AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-initramfs-${MACHINE}.cpio.gz"
LABELS = "poky"
APPEND += "root=/dev/ram0 "

EXCLUDE_FROM_WORLD = "1"

do_bootimg[depends] += "poky-image-minimal-initramfs:do_rootfs"

inherit bootimg

do_bootimg_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}

