#
# Copyright (C) 2008 Intel Corporation.
#
DESCRIPTION = "Moblin Direct Disk Image"

IMAGE_FEATURES += "apps-console-core ${NETBOOK_IMAGE_FEATURES}"

inherit boot-directdisk
inherit moblin-image

do_bootdirectdisk_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-netbook-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "moblin-image-netbook:do_rootfs"
