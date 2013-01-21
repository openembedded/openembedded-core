python read_subpackage_metadata () {
    import oe.packagedata

    pn = d.getVar('PN', True)
    data = oe.packagedata.read_pkgdata(pn, d)

    for key in data.keys():
        d.setVar(key, data[key])

    for pkg in d.getVar('PACKAGES', True).split():
        sdata = oe.packagedata.read_subpkgdata(pkg, d)
        for key in sdata.keys():
            if key == "PN":
                if sdata[key] != pn:
                    bb.fatal("Recipe %s is trying to create package %s which was already written by recipe %s. This will cause corruption, please resolve this and only provide the package from one recipe or the other or only build one of the recipes." % (pn, pkg, sdata[key]))
                continue
            d.setVar(key, sdata[key])
}
