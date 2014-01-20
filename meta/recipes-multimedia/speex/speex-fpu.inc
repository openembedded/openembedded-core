def get_speex_fpu_setting(bb, d):
     if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
             return "--enable-fixed-point --disable-float-api --disable-vbr"
     return ""
