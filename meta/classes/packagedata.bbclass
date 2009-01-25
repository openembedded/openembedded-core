def packaged(pkg, d):
	import os, bb
	return os.access(get_subpkgedata_fn(pkg, d) + '.packaged', os.R_OK)

def read_pkgdatafile(fn):
	pkgdata = {}

	def decode(str):
		import codecs
		c = codecs.getdecoder("string_escape")
		return c(str)[0]

	import os
	if os.access(fn, os.R_OK):
		import re
		f = file(fn, 'r')
		lines = f.readlines()
		f.close()
		r = re.compile("([^:]+):\s*(.*)")
		for l in lines:
			m = r.match(l)
			if m:
				pkgdata[m.group(1)] = decode(m.group(2))

	return pkgdata

def get_subpkgedata_fn(pkg, d):
	import bb, os
	archs = bb.data.expand("${PACKAGE_ARCHS}", d).split(" ")
	archs.reverse()
	pkgdata = bb.data.expand('${TMPDIR}/pkgdata/', d)
	targetdir = bb.data.expand('${TARGET_VENDOR}-${TARGET_OS}/runtime/', d)
	for arch in archs:
		fn = pkgdata + arch + targetdir + pkg
		if os.path.exists(fn):
			return fn
	return bb.data.expand('${PKGDATA_DIR}/runtime/%s' % pkg, d)

def has_subpkgdata(pkg, d):
	import bb, os
	return os.access(get_subpkgedata_fn(pkg, d), os.R_OK)

def read_subpkgdata(pkg, d):
	import bb
	return read_pkgdatafile(get_subpkgedata_fn(pkg, d))

def has_pkgdata(pn, d):
	import bb, os
	fn = bb.data.expand('${PKGDATA_DIR}/%s' % pn, d)
	return os.access(fn, os.R_OK)

def read_pkgdata(pn, d):
	import bb
	fn = bb.data.expand('${PKGDATA_DIR}/%s' % pn, d)
	return read_pkgdatafile(fn)

python read_subpackage_metadata () {
	import bb
	data = read_pkgdata(bb.data.getVar('PN', d, 1), d)

	for key in data.keys():
		bb.data.setVar(key, data[key], d)

	for pkg in bb.data.getVar('PACKAGES', d, 1).split():
		sdata = read_subpkgdata(pkg, d)
		for key in sdata.keys():
			bb.data.setVar(key, sdata[key], d)
}


#
# Collapse FOO_pkg variables into FOO
#
def read_subpkgdata_dict(pkg, d):
	import bb
	ret = {}
	subd = read_pkgdatafile(get_subpkgedata_fn(pkg, d))
	for var in subd:
		newvar = var.replace("_" + pkg, "")
		ret[newvar] = subd[var]
	return ret

