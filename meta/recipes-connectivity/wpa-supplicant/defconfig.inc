def get_defconfig(bb, d):
	if d.getVar('INCOMPATIBLE_LICENSE', 1) in [ 'GPLv3' ]:
		return "-nogplv3"
	return ""
