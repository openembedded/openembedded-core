#
# Copyright (C) 2008 Intel Corporation.
#

AUTO_SYSLINUXCFG = "1"
INITRD = "${DEPLOY_DIR_IMAGE}/moblin-image-minimal-initramfs-${MACHINE}.cpio.gz"
APPEND += "root=/dev/ram0 "
TIMEOUT = "10"

EXCLUDE_FROM_WORLD = "1"

LABELS += "boot install"

do_bootimg[depends] += "moblin-image-minimal-initramfs:do_rootfs"

inherit bootimg

do_bootimg_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}

ROOTFS = "${DEPLOY_DIR_IMAGE}/${MOBLIN_LIVE_IMAGENAME}-${MACHINE}.ext3"

do_bootimg[depends] += "${MOBLIN_LIVE_IMAGENAME}:do_rootfs"
