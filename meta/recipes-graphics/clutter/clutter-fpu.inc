
def get_clutter_fpu_setting(bb, d):
	if d.getVar('TARGET_FPU', 1) in [ 'soft' ]:
		return "--without-fpu"
	return ""

