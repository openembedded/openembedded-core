inherit boot-directdisk

do_bootdirectdisk_prepend () {
	import bb
	fstypes = bb.data.getVar('IMAGE_FSTYPES', d, True)
	if 'ext3' not in fstypes:
		bb.msg.fatal(bb.msg.domain.Build, "ext3 not in IMAGE_FSTYPES")
}