
def get_gcc_fpu_setting(bb, d):
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		return "--with-float=soft"
	return ""

