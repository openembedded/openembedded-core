
def get_alsa_fpu_setting(bb, d):
	if d.getVar('TARGET_FPU', 1) in [ 'soft' ]:
		return "--with-softfloat"
	return ""

