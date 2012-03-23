SSTATE_VERSION = "2"

SSTATE_MANIFESTS ?= "${TMPDIR}/sstate-control"
SSTATE_MANFILEBASE = "${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-"
SSTATE_MANFILEPREFIX = "${SSTATE_MANFILEBASE}${PN}"


SSTATE_PKGARCH    = "${PACKAGE_ARCH}"
SSTATE_PKGSPEC    = "sstate-${PN}-${PACKAGE_ARCH}${TARGET_VENDOR}-${TARGET_OS}-${PV}-${PR}-${SSTATE_PKGARCH}-${SSTATE_VERSION}-"
SSTATE_PKGNAME    = "${SSTATE_PKGSPEC}${BB_TASKHASH}"
SSTATE_PKG        = "${SSTATE_DIR}/${SSTATE_PKGNAME}"

SSTATE_SCAN_FILES ?= "*.la *-config *_config"
SSTATE_SCAN_CMD ?= 'find ${SSTATE_BUILDDIR} \( -name "${@"\" -o -name \"".join(d.getVar("SSTATE_SCAN_FILES", True).split())}" \) -type f'

BB_HASHFILENAME = "${SSTATE_PKGNAME}"

SSTATE_MANMACH ?= "${SSTATE_PKGARCH}"

SSTATEPREINSTFUNCS ?= ""
SSTATEPOSTINSTFUNCS ?= ""

python () {
    if bb.data.inherits_class('native', d):
        d.setVar('SSTATE_PKGARCH', d.getVar('BUILD_ARCH'))
    elif bb.data.inherits_class('cross', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${BUILD_ARCH}_${TUNE_PKGARCH}"))
        d.setVar('SSTATE_MANMACH', d.expand("${BUILD_ARCH}_${MACHINE}"))
    elif bb.data.inherits_class('crosssdk', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${BUILD_ARCH}_${PACKAGE_ARCH}"))
    elif bb.data.inherits_class('nativesdk', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${SDK_ARCH}"))
    elif bb.data.inherits_class('cross-canadian', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${SDK_ARCH}_${PACKAGE_ARCH}"))
    else:
        d.setVar('SSTATE_MANMACH', d.expand("${MACHINE}"))

    # These classes encode staging paths into their scripts data so can only be
    # reused if we manipulate the paths
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d) or bb.data.inherits_class('crosssdk', d):
        scan_cmd = "grep -Irl ${STAGING_DIR} ${SSTATE_BUILDDIR}"
        d.setVar('SSTATE_SCAN_CMD', scan_cmd)

    unique_tasks = set((d.getVar('SSTATETASKS', True) or "").split())
    d.setVar('SSTATETASKS', " ".join(unique_tasks))
    namemap = []
    for task in unique_tasks:
        namemap.append(d.getVarFlag(task, 'sstate-name'))
        d.prependVarFlag(task, 'prefuncs', "sstate_task_prefunc ")
        d.appendVarFlag(task, 'postfuncs', " sstate_task_postfunc")
    d.setVar('SSTATETASKNAMES', " ".join(namemap))
}

def sstate_init(name, task, d):
    ss = {}
    ss['task'] = task
    ss['name'] = name
    ss['dirs'] = []
    ss['plaindirs'] = []
    ss['lockfiles'] = []
    ss['lockfiles-shared'] = []
    return ss

def sstate_state_fromvars(d, task = None):
    if task is None:
        task = d.getVar('BB_CURRENTTASK', True)
        if not task:
            bb.fatal("sstate code running without task context?!")
        task = task.replace("_setscene", "")

    name = d.getVarFlag("do_" + task, 'sstate-name', True)
    inputs = (d.getVarFlag("do_" + task, 'sstate-inputdirs', True) or "").split()
    outputs = (d.getVarFlag("do_" + task, 'sstate-outputdirs', True) or "").split()
    plaindirs = (d.getVarFlag("do_" + task, 'sstate-plaindirs', True) or "").split()
    lockfiles = (d.getVarFlag("do_" + task, 'sstate-lockfile', True) or "").split()
    lockfilesshared = (d.getVarFlag("do_" + task, 'sstate-lockfile-shared', True) or "").split()
    interceptfuncs = (d.getVarFlag("do_" + task, 'sstate-interceptfuncs', True) or "").split()
    if not name or len(inputs) != len(outputs):
        bb.fatal("sstate variables not setup correctly?!")

    ss = sstate_init(name, task, d)
    for i in range(len(inputs)):
        sstate_add(ss, inputs[i], outputs[i], d)
    ss['lockfiles'] = lockfiles
    ss['lockfiles-shared'] = lockfilesshared
    ss['plaindirs'] = plaindirs
    ss['interceptfuncs'] = interceptfuncs
    return ss

def sstate_add(ss, source, dest, d):
    srcbase = os.path.basename(source)
    ss['dirs'].append([srcbase, source, dest])
    return ss

def sstate_install(ss, d):
    import oe.path

    sharedfiles = []
    shareddirs = []
    bb.mkdirhier(d.expand("${SSTATE_MANIFESTS}"))
    manifest = d.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'])

    if os.access(manifest, os.R_OK):
        bb.fatal("Package already staged (%s)?!" % manifest)

    locks = []
    for lock in ss['lockfiles-shared']:
        locks.append(bb.utils.lockfile(lock, True))
    for lock in ss['lockfiles']:
        locks.append(bb.utils.lockfile(lock))

    for state in ss['dirs']:
        oe.path.copytree(state[1], state[2])
        for walkroot, dirs, files in os.walk(state[1]):
            bb.debug(2, "Staging files from %s to %s" % (state[1], state[2]))
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], state[2])
                #bb.debug(2, "Staging %s to %s" % (srcpath, dstpath))
                sharedfiles.append(dstpath)
            for dir in dirs:
                srcdir = os.path.join(walkroot, dir)
                dstdir = srcdir.replace(state[1], state[2])
                #bb.debug(2, "Staging %s to %s" % (srcdir, dstdir))
                if not dstdir.endswith("/"):
                    dstdir = dstdir + "/"
                shareddirs.append(dstdir)
    f = open(manifest, "w")
    for file in sharedfiles:
        f.write(file + "\n")
    # We want to ensure that directories appear at the end of the manifest
    # so that when we test to see if they should be deleted any contents
    # added by the task will have been removed first.
    dirs = sorted(shareddirs, key=len)
    # Must remove children first, which will have a longer path than the parent
    for di in reversed(dirs):
        f.write(di + "\n")
    f.close()

    for postinst in (d.getVar('SSTATEPOSTINSTFUNCS', True) or '').split():
        bb.build.exec_func(postinst, d)

    for lock in locks:
        bb.utils.unlockfile(lock)

def sstate_installpkg(ss, d):
    import oe.path

    def prepdir(dir):
        # remove dir if it exists, ensure any parent directories do exist
        if os.path.exists(dir):
            oe.path.remove(dir)
        bb.mkdirhier(dir)
        oe.path.remove(dir)

    sstateinst = d.expand("${WORKDIR}/sstate-install-%s/" % ss['name'])
    sstatepkg = d.getVar('SSTATE_PKG', True) + '_' + ss['name'] + ".tgz"

    if not os.path.exists(sstatepkg):
       pstaging_fetch(sstatepkg, d)

    if not os.path.isfile(sstatepkg):
        bb.note("Staging package %s does not exist" % sstatepkg)
        return False

    sstate_clean(ss, d)

    d.setVar('SSTATE_INSTDIR', sstateinst)
    d.setVar('SSTATE_PKG', sstatepkg)

    for preinst in (d.getVar('SSTATEPREINSTFUNCS', True) or '').split():
        bb.build.exec_func(preinst, d)

    bb.build.exec_func('sstate_unpack_package', d)

    # Fixup hardcoded paths
    fixmefn =  sstateinst + "fixmepath"
    if os.path.isfile(fixmefn):
        staging = d.getVar('STAGING_DIR', True)
        staging_target = d.getVar('STAGING_DIR_TARGET', True)
        staging_host = d.getVar('STAGING_DIR_HOST', True)
        fixmefd = open(fixmefn, "r")
        fixmefiles = fixmefd.readlines()
        fixmefd.close()
        for file in fixmefiles:
            os.system("sed -i -e s:FIXMESTAGINGDIRTARGET:%s:g %s" % (staging_target, sstateinst + file))
            os.system("sed -i -e s:FIXMESTAGINGDIRHOST:%s:g %s" % (staging_host, sstateinst + file))
            os.system("sed -i -e s:FIXMESTAGINGDIR:%s:g %s" % (staging, sstateinst + file))
        # Need to remove this or we'd copy it into the target directory and may 
        # conflict with another writer
        os.remove(fixmefn)

    for state in ss['dirs']:
        prepdir(state[1])
        os.rename(sstateinst + state[0], state[1])
    sstate_install(ss, d)

    for plain in ss['plaindirs']:
        workdir = d.getVar('WORKDIR', True)
        src = sstateinst + "/" + plain.replace(workdir, '')
        dest = plain
        bb.mkdirhier(src)
        prepdir(dest)
        os.rename(src, dest)

    return True

def sstate_clean_cachefile(ss, d):
    import oe.path

    sstatepkgdir = d.getVar('SSTATE_DIR', True)
    sstatepkgfile = sstatepkgdir + '/' + d.getVar('SSTATE_PKGSPEC', True) + "*_" + ss['name'] + ".tgz*"
    bb.note("Removing %s" % sstatepkgfile)
    oe.path.remove(sstatepkgfile)

def sstate_clean_cachefiles(d):
    for task in (d.getVar('SSTATETASKS', True) or "").split():
        ss = sstate_state_fromvars(d, task[3:])
        sstate_clean_cachefile(ss, d)

def sstate_clean_manifest(manifest, d):
    import oe.path

    mfile = open(manifest)
    entries = mfile.readlines()
    mfile.close()

    for entry in entries:
        entry = entry.strip()
        bb.debug(2, "Removing manifest: %s" % entry)
        # We can race against another package populating directories as we're removing them
        # so we ignore errors here.
        try:
            if entry.endswith("/"):
               if os.path.islink(entry[:-1]):
                  os.remove(entry[:-1])
               elif os.path.exists(entry) and len(os.listdir(entry)) == 0:
                  os.rmdir(entry[:-1])
            else:
                oe.path.remove(entry)
        except OSError:
            pass

    oe.path.remove(manifest)

def sstate_clean(ss, d):
    import oe.path

    manifest = d.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'])

    if os.path.exists(manifest):
        locks = []
        for lock in ss['lockfiles-shared']:
            locks.append(bb.utils.lockfile(lock))
        for lock in ss['lockfiles']:
            locks.append(bb.utils.lockfile(lock))

        sstate_clean_manifest(manifest, d)

        for lock in locks:
            bb.utils.unlockfile(lock)

    stfile = d.getVar("STAMP", True) + ".do_" + ss['task']
    extrainf = d.getVarFlag("do_" + ss['task'], 'stamp-extra-info', True)
    oe.path.remove(stfile)
    oe.path.remove(stfile + "_setscene")
    if extrainf:
        oe.path.remove(stfile + ".*" + extrainf)
        oe.path.remove(stfile + "_setscene" + ".*" + extrainf)
    else:
        oe.path.remove(stfile + ".*")
        oe.path.remove(stfile + "_setscene" + ".*")

CLEANFUNCS += "sstate_cleanall"

python sstate_cleanall() {
    import fnmatch

    bb.note("Removing shared state for package %s" % d.getVar('PN', True))

    manifest_dir = d.getVar('SSTATE_MANIFESTS', True)
    manifest_prefix = d.getVar("SSTATE_MANFILEPREFIX", True)
    manifest_pattern = os.path.basename(manifest_prefix) + ".*"

    if not os.path.exists(manifest_dir):
        return

    for manifest in (os.listdir(manifest_dir)):
        if fnmatch.fnmatch(manifest, manifest_pattern):
             name = manifest.replace(manifest_pattern[:-1], "")
             namemap = d.getVar('SSTATETASKNAMES', True).split()
             tasks = d.getVar('SSTATETASKS', True).split()
             if name not in namemap:
                  continue
             taskname = tasks[namemap.index(name)]
             shared_state = sstate_state_fromvars(d, taskname[3:])
             sstate_clean(shared_state, d)
}

def sstate_hardcode_path(d):
	# Need to remove hardcoded paths and fix these when we install the
	# staging packages.

	staging = d.getVar('STAGING_DIR', True)
	staging_target = d.getVar('STAGING_DIR_TARGET', True)
	staging_host = d.getVar('STAGING_DIR_HOST', True)
	sstate_builddir = d.getVar('SSTATE_BUILDDIR', True)

	if bb.data.inherits_class('native', d) or bb.data.inherits_class('nativesdk', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('cross-canadian', d):
		sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIR:g'" % (staging)
	elif bb.data.inherits_class('cross', d):
		sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIRTARGET:g; s:%s:FIXMESTAGINGDIR:g'" % (staging_target, staging)
	else:
		sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIRHOST:g'" % (staging_host)
	
	sstate_scan_cmd = d.getVar('SSTATE_SCAN_CMD', True)
	sstate_filelist_cmd = "tee %sfixmepath" % (sstate_builddir)

	# fixmepath file needs relative paths, drop sstate_builddir prefix
	sstate_filelist_relative_cmd = "sed -i -e 's:^%s::g' %sfixmepath" % (sstate_builddir, sstate_builddir)

	sstate_hardcode_cmd = "%s | %s | xargs %s" % (sstate_scan_cmd, sstate_filelist_cmd, sstate_sed_cmd)

	print "Removing hardcoded paths from sstate package: '%s'" % (sstate_hardcode_cmd)
	os.system(sstate_hardcode_cmd)
	print "Replacing absolute paths in fixmepath file: '%s'" % (sstate_filelist_relative_cmd)
	os.system(sstate_filelist_relative_cmd)

def sstate_package(ss, d):
    import oe.path

    def make_relative_symlink(path, outputpath, d):
        # Replace out absolute TMPDIR paths in symlinks with relative ones
        if not os.path.islink(path):
            return
        link = os.readlink(path)
        if not os.path.isabs(link):
            return
        if not link.startswith(tmpdir):
            return

        depth = link.rpartition(tmpdir)[2].count('/')
        base = link.partition(tmpdir)[2].strip()
        while depth > 1:
            base = "../" + base
            depth -= 1

        bb.debug(2, "Replacing absolute path %s with relative path %s" % (link, base))
        os.remove(path)
        os.symlink(base, path)

    tmpdir = d.getVar('TMPDIR', True)

    sstatebuild = d.expand("${WORKDIR}/sstate-build-%s/" % ss['name'])
    sstatepkg = d.getVar('SSTATE_PKG', True) + '_'+ ss['name'] + ".tgz"
    bb.mkdirhier(sstatebuild)
    bb.mkdirhier(os.path.dirname(sstatepkg))
    for state in ss['dirs']:
        srcbase = state[0].rstrip("/").rsplit('/', 1)[0]
        for walkroot, dirs, files in os.walk(state[1]):
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], sstatebuild + state[0])
                make_relative_symlink(srcpath, dstpath, d)
            for dir in dirs:
                srcpath = os.path.join(walkroot, dir)
                dstpath = srcpath.replace(state[1], sstatebuild + state[0])
                make_relative_symlink(srcpath, dstpath, d)
        bb.debug(2, "Preparing tree %s for packaging at %s" % (state[1], sstatebuild + state[0]))
        oe.path.copytree(state[1], sstatebuild + state[0])

    workdir = d.getVar('WORKDIR', True)
    for plain in ss['plaindirs']:
        pdir = plain.replace(workdir, sstatebuild)
        bb.mkdirhier(plain)
        bb.mkdirhier(pdir)
        oe.path.copytree(plain, pdir)

    d.setVar('SSTATE_BUILDDIR', sstatebuild)
    d.setVar('SSTATE_PKG', sstatepkg)
    sstate_hardcode_path(d)
    bb.build.exec_func('sstate_create_package', d)
    
    bb.siggen.dump_this_task(sstatepkg + ".siginfo", d)

    return

def pstaging_fetch(sstatepkg, d):
    import bb.fetch2

    # Only try and fetch if the user has configured a mirror
    mirrors = d.getVar('SSTATE_MIRRORS', True)
    if not mirrors:
        return

    # Copy the data object and override DL_DIR and SRC_URI
    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)

    dldir = localdata.expand("${SSTATE_DIR}")
    srcuri = "file://" + os.path.basename(sstatepkg)

    bb.mkdirhier(dldir)

    localdata.setVar('DL_DIR', dldir)
    localdata.setVar('PREMIRRORS', mirrors)
    localdata.setVar('SRC_URI', srcuri)

    # Try a fetch from the sstate mirror, if it fails just return and
    # we will build the package
    try:
        fetcher = bb.fetch2.Fetch([srcuri], localdata, cache=False)
        fetcher.download()        

        # Need to optimise this, if using file:// urls, the fetcher just changes the local path
        # For now work around by symlinking
        localpath = bb.data.expand(fetcher.localpath(srcuri), localdata)
        if localpath != sstatepkg and os.path.exists(localpath) and not os.path.exists(sstatepkg):
            os.symlink(localpath, sstatepkg)

    except bb.fetch2.BBFetchException:
        pass

def sstate_setscene(d):
    shared_state = sstate_state_fromvars(d)
    accelerate = sstate_installpkg(shared_state, d)
    if not accelerate:
        raise bb.build.FuncFailed("No suitable staging package found")

python sstate_task_prefunc () {
    shared_state = sstate_state_fromvars(d)
    sstate_clean(shared_state, d)
}

python sstate_task_postfunc () {
    shared_state = sstate_state_fromvars(d)
    sstate_install(shared_state, d)
    for intercept in shared_state['interceptfuncs']:
        bb.build.exec_func(intercept, d)
    sstate_package(shared_state, d)
}
  

#
# Shell function to generate a sstate package from a directory
# set as SSTATE_BUILDDIR
#
sstate_create_package () {
	cd ${SSTATE_BUILDDIR}
	TFILE=`mktemp ${SSTATE_PKG}.XXXXXXXX`
	# Need to handle empty directories
	if [ "$(ls -A)" ]; then
		tar -czf $TFILE *
	else
		tar -cz --file=$TFILE --files-from=/dev/null
	fi
	mv $TFILE ${SSTATE_PKG}

	cd ${WORKDIR}
	rm -rf ${SSTATE_BUILDDIR}
}

#
# Shell function to decompress and prepare a package for installation
#
sstate_unpack_package () {
	mkdir -p ${SSTATE_INSTDIR}
	cd ${SSTATE_INSTDIR}
	tar -xvzf ${SSTATE_PKG}
}

BB_HASHCHECK_FUNCTION = "sstate_checkhashes"

def sstate_checkhashes(sq_fn, sq_task, sq_hash, sq_hashfn, d):

    ret = []
    # This needs to go away, FIXME
    mapping = {
        "do_populate_sysroot" : "populate-sysroot",
        "do_populate_lic" : "populate-lic",
        "do_package_write_ipk" : "deploy-ipk",
        "do_package_write_deb" : "deploy-deb",
        "do_package_write_rpm" : "deploy-rpm",
        "do_package" : "package",
        "do_deploy" : "deploy",
    }

    for task in range(len(sq_fn)):
        sstatefile = d.expand("${SSTATE_DIR}/" + sq_hashfn[task] + "_" + mapping[sq_task[task]] + ".tgz")
        sstatefile = sstatefile.replace("${BB_TASKHASH}", sq_hash[task])
        if os.path.exists(sstatefile):
            bb.debug(2, "SState: Found valid sstate file %s" % sstatefile)
            ret.append(task)
            continue
        else:
            bb.debug(2, "SState: Looked for but didn't find file %s" % sstatefile)

    mirrors = d.getVar("SSTATE_MIRRORS", True)
    if mirrors:
        # Copy the data object and override DL_DIR and SRC_URI
        localdata = bb.data.createCopy(d)
        bb.data.update_data(localdata)

        dldir = localdata.expand("${SSTATE_DIR}")
        localdata.setVar('DL_DIR', dldir)
        localdata.setVar('PREMIRRORS', mirrors)

        bb.debug(2, "SState using premirror of: %s" % mirrors)

        for task in range(len(sq_fn)):
            if task in ret:
                continue

            sstatefile = d.expand("${SSTATE_DIR}/" + sq_hashfn[task] + "_" + mapping[sq_task[task]] + ".tgz")
            sstatefile = sstatefile.replace("${BB_TASKHASH}", sq_hash[task])

            srcuri = "file://" + os.path.basename(sstatefile)
            localdata.setVar('SRC_URI', srcuri)
            bb.debug(2, "SState: Attempting to fetch %s" % srcuri)

            try:
                fetcher = bb.fetch2.Fetch(srcuri.split(), localdata)
                fetcher.checkstatus()
                bb.debug(2, "SState: Successful fetch test for %s" % srcuri)
                ret.append(task)
            except:
                bb.debug(2, "SState: Unsuccessful fetch test for %s" % srcuri)
                pass     

    return ret

