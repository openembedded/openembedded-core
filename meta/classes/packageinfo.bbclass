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
        dirs = os.listdir(tmpdir + '/work/')
        pkgsplit_dir = tmpdir + '/work/'
        items = {}
        passing = ''
        for directories in dirs:
                temp_dirs = os.listdir(pkgsplit_dir + directories)
                for temps1 in temp_dirs:
                        if os.path.exists(pkgsplit_dir + directories + '/' + temps1 + '/' + os.listdir(pkgsplit_dir + directories + '/' + temps1)[0] + '/packages-split'):
                                subs = pkgsplit_dir + directories + '/' + temps1 + '/' + os.listdir(pkgsplit_dir + directories + '/' + temps1)[0] + '/packages-split'
                                for temps in os.listdir(subs):
                                        items[temps] = {}
                                        for path, dirs, files in os.walk(pkgsplit_dir + directories + '/' + temps1 + '/' + os.listdir(pkgsplit_dir + directories + '/' + temps1)[0] + '/packages-split' + '/' + temps):                                                   
                                                        file_list = []
                                                        if os.listdir(path) != []:
                                                                items[temps][path] = []                                                   
                                                                for f in files:
                                                                        file_list.append(f)
                                                                items[temps][path].append(file_list)
                                           
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
                                if pkgname in items:
                                        sdata['FILES_INFO'] = items[pkgname]        
                                pkginfolist.append(sdata)
                            except Exception as e:
                                bb.warn("Failed to read pkgdata file %s: %s: %s" % (pkgdatafile, e.__class__, str(e)))
        bb.event.fire(bb.event.PackageInfo(pkginfolist), e.data)
}

addhandler packageinfo_handler
