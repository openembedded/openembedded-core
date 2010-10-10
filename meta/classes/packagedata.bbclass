python read_subpackage_metadata () {
	import oe.packagedata

	data = oe.packagedata.read_pkgdata(bb.data.getVar('PN', d, 1), d)

	for key in data.keys():
		bb.data.setVar(key, data[key], d)

	for pkg in bb.data.getVar('PACKAGES', d, 1).split():
		sdata = oe.packagedata.read_subpkgdata(pkg, d)
		for key in sdata.keys():
			bb.data.setVar(key, sdata[key], d)
}
