# parse kernel ABI version out of <linux/version.h>
def get_kernelversion(p):
    import re, os

    fn = p + '/include/linux/utsrelease.h'
    if not os.path.isfile(fn):
        fn = p + '/include/linux/version.h'

    import re
    try:
        f = open(fn, 'r')
    except IOError:
        return None

    l = f.readlines()
    f.close()
    r = re.compile("#define UTS_RELEASE \"(.*)\"")
    for s in l:
        m = r.match(s)
        if m:
            return m.group(1)
    return None

def get_kernelmajorversion(p):
	import re
	r = re.compile("([0-9]+\.[0-9]+).*")
	m = r.match(p);
	if m:
		return m.group(1)
	return None

def linux_module_packages(s, d):
	import bb, os.path
	suffix = ""
	return " ".join(map(lambda s: "kernel-module-%s%s" % (s.lower().replace('_', '-').replace('@', '+'), suffix), s.split()))

# that's all

