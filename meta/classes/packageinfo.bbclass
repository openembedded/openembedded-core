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
                            continue
                        sdata = oe.packagedata.read_pkgdatafile(root + pkgname)
                        sdata['PKG'] = pkgname
                        pkgrename = sdata['PKG_%s' % pkgname]
                        pkgv = sdata['PKGV'].replace('-', '+')
                        pkgr = sdata['PKGR']
                        # We found there are some renaming issue with certain architecture.
                        # For example, armv7a-vfp-neon, it will use armv7a in the rpm file. This is the workaround for it.
                        arch_tmp = arch.split('-')[0]
                        if os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgname + '-' + pkgv + '-' + pkgr + '.' + arch + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgname + '-' + pkgv + '-' + pkgr + '.' + arch_tmp + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgrename + '-' + pkgv + '-' + pkgr + '.' + arch + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgrename + '-' + pkgv + '-' + pkgr + '.' + arch_tmp + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgname + '_' + pkgv + '-' + pkgr + '_' + arch + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgname + '_' + pkgv + '-' + pkgr + '_' + arch_tmp + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgrename + '_' + pkgv + '-' + pkgr + '_' + arch + '.' + packaging) or \
                           os.path.exists(deploy_dir + '/' + arch + '/' + \
                                          pkgrename + '_' + pkgv + '-' + pkgr + '_' + arch_tmp + '.' + packaging):
                            pkginfolist.append(sdata)
        bb.event.fire(bb.event.PackageInfo(pkginfolist), e.data)
}

addhandler packageinfo_handler
