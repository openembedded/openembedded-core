python packageinfo_handler () {
    if isinstance(e, bb.event.RequestPackageInfo):
        import oe.packagedata
        pkginfolist = []
        tmpdir = e.data.getVar('TMPDIR', True)
        target_vendor = e.data.getVar('TARGET_VENDOR', True)
        target_os = e.data.getVar('TARGET_OS', True)
        package_archs = e.data.getVar('PACKAGE_ARCHS', True)
        packaging = e.data.getVar('PACKAGE_CLASSES', True).split()[0].split('_')[1]
        deploy_dir = e.data.getVar('DEPLOY_DIR', True) + '/' + packaging
                                           
        for arch in package_archs.split():
            pkgdata_dir = tmpdir + '/pkgdata/' + arch + target_vendor + '-' + target_os + '/runtime/'
            if os.path.exists(pkgdata_dir):
                for root, dirs, files in os.walk(pkgdata_dir):
                    for pkgname in files:
                        if pkgname.endswith('.packaged'):
                            pkgname = pkgname[:-9]
                            pkgdatafile = root + pkgname
                            try:
                                sdata = oe.packagedata.read_pkgdatafile(pkgdatafile)
                                sdata['PKG'] = pkgname
                                pkginfolist.append(sdata)
                            except Exception as e:
                                bb.warn("Failed to read pkgdata file %s: %s: %s" % (pkgdatafile, e.__class__, str(e)))
        bb.event.fire(bb.event.PackageInfo(pkginfolist), e.data)
}

addhandler packageinfo_handler
