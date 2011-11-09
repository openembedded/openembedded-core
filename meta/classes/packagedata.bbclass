python read_subpackage_metadata () {
	import oe.packagedata

	data = oe.packagedata.read_pkgdata(d.getVar('PN', 1), d)

	for key in data.keys():
		d.setVar(key, data[key])

	for pkg in d.getVar('PACKAGES', 1).split():
		sdata = oe.packagedata.read_subpkgdata(pkg, d)
		for key in sdata.keys():
			d.setVar(key, sdata[key])
}
