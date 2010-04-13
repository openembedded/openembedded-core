DESCRIPTION = "Sato Direct Disk Image"

inherit boot-directdisk

do_bootdirectdisk_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sato-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "poky-image-sato:do_rootfs"
