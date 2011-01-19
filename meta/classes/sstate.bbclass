SSTATE_VERSION = "1"

SSTATE_MANIFESTS = "${TMPDIR}/sstate-control"
SSTATE_MANFILEBASE = "${SSTATE_MANIFESTS}/manifest-${SSTATE_PKGARCH}-"
SSTATE_MANFILEPREFIX = "${SSTATE_MANFILEBASE}${PN}"


SSTATE_PKGARCH    = "${MULTIMACH_ARCH}"
SSTATE_PKGSPEC    = "sstate-${PN}-${MULTIMACH_ARCH}${TARGET_VENDOR}-${TARGET_OS}-${PV}-${PR}-${SSTATE_PKGARCH}-${SSTATE_VERSION}-"
SSTATE_PKGNAME    = "${SSTATE_PKGSPEC}${BB_TASKHASH}"
SSTATE_PKG        = "${SSTATE_DIR}/${SSTATE_PKGNAME}"

SSTATE_SCAN_CMD ?= "find ${SSTATE_BUILDDIR} \( -name "*.la" -o -name "*-config" \) -type f"

BB_HASHFILENAME = "${SSTATE_PKGNAME}"

python () {
    if bb.data.inherits_class('native', d):
        bb.data.setVar('SSTATE_PKGARCH', bb.data.getVar('BUILD_ARCH', d), d)
    elif bb.data.inherits_class('cross', d) or bb.data.inherits_class('crosssdk', d):
        bb.data.setVar('SSTATE_PKGARCH', bb.data.expand("${BUILD_ARCH}_${BASE_PACKAGE_ARCH}", d), d)
    elif bb.data.inherits_class('nativesdk', d):
        bb.data.setVar('SSTATE_PKGARCH', bb.data.expand("${SDK_ARCH}", d), d)
    elif bb.data.inherits_class('cross-canadian', d):
        bb.data.setVar('SSTATE_PKGARCH', bb.data.expand("${SDK_ARCH}_${BASE_PACKAGE_ARCH}", d), d)

    # These classes encode staging paths into their scripts data so can only be
    # reused if we manipulate the paths
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d) or bb.data.inherits_class('crosssdk', d):
        scan_cmd = "grep -Irl ${STAGING_DIR} ${SSTATE_BUILDDIR}"
        bb.data.setVar('SSTATE_SCAN_CMD', scan_cmd, d)

    namemap = []
    for task in (bb.data.getVar('SSTATETASKS', d, True) or "").split():
        namemap.append(bb.data.getVarFlag(task, 'sstate-name', d))
        funcs = bb.data.getVarFlag(task, 'prefuncs', d) or ""
        funcs = "sstate_task_prefunc " + funcs
        bb.data.setVarFlag(task, 'prefuncs', funcs, d)
        funcs = bb.data.getVarFlag(task, 'postfuncs', d) or ""
        funcs = funcs + " sstate_task_postfunc"
        bb.data.setVarFlag(task, 'postfuncs', funcs, d)
    d.setVar('SSTATETASKNAMES', " ".join(namemap))
}

def sstate_init(name, d):
    ss = {}
    ss['name'] = name
    ss['dirs'] = []
    ss['plaindirs'] = []
    ss['lockfiles'] = []
    return ss

def sstate_state_fromvars(d, task = None):
    if task is None:
        task = bb.data.getVar('BB_CURRENTTASK', d, True)
        if not task:
            bb.fatal("sstate code running without task context?!")
        task = task.replace("_setscene", "")

    name = bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-name', d), d)
    inputs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-inputdirs', d) or "", d)).split()
    outputs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-outputdirs', d) or "", d)).split()
    plaindirs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-plaindirs', d) or "", d)).split()
    lockfiles = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-lockfile', d) or "", d)).split()
    interceptfuncs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-interceptfuncs', d) or "", d)).split()
    if not name or len(inputs) != len(outputs):
        bb.fatal("sstate variables not setup correctly?!")

    ss = sstate_init(name, d)
    for i in range(len(inputs)):
        sstate_add(ss, inputs[i], outputs[i], d)
    ss['lockfiles'] = lockfiles
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
    bb.mkdirhier(bb.data.expand("${SSTATE_MANIFESTS}", d))
    manifest = bb.data.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'], d)

    if os.access(manifest, os.R_OK):
        bb.fatal("Package already staged (%s)?!" % manifest)

    def checkmanifest(pn, task):
        return os.access(bb.data.expand("${SSTATE_MANFILEBASE}%s.%s" % (pn, task), d), os.R_OK)

    skipinst = False
    pn = d.getVar("PN", True)
    if pn == "gcc-cross-initial":
        if checkmanifest("gcc-cross", "populate-sysroot"):
            skipinst = True
        if checkmanifest("gcc-cross-intermediate", "populate-sysroot"):
            skipinst = True
    elif pn == "gcc-cross-intermediate":
        if checkmanifest("gcc-cross", "populate-sysroot"):
            skipinst = True
    elif pn == "glibc-initial":
        if checkmanifest("glibc", "populate-sysroot"):
            skipinst = True
    elif pn == "eglibc-initial":
        if checkmanifest("eglibc", "populate-sysroot"):
            skipinst = True

    if skipinst:
        bb.note("Not staging %s.%s as sysroot already contains better functionality" % (pn, ss['name']))
        return

    locks = []
    for lock in ss['lockfiles']:
        locks.append(bb.utils.lockfile(lock))

    for state in ss['dirs']:
        oe.path.copytree(state[1], state[2])
        for walkroot, dirs, files in os.walk(state[1]):
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], state[2])
                bb.debug(2, "Staging %s to %s" % (srcpath, dstpath))
                sharedfiles.append(dstpath)
            for dir in dirs:
                srcdir = os.path.join(walkroot, dir)
                dstdir = srcdir.replace(state[1], state[2])
                bb.debug(2, "Staging %s to %s" % (srcdir, dstdir))
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

    for lock in locks:
        bb.utils.unlockfile(lock)

def sstate_installpkg(ss, d):
    import oe.path

    sstateinst = bb.data.expand("${WORKDIR}/sstate-install-%s/" % ss['name'], d)
    sstatepkg = bb.data.getVar('SSTATE_PKG', d, True) + '_' + ss['name'] + ".tgz"

    if not os.path.exists(sstatepkg):
       pstaging_fetch(sstatepkg, d)

    if not os.path.isfile(sstatepkg):
        bb.note("Staging package %s does not exist" % sstatepkg)
        return False

    sstate_clean(ss, d)

    bb.data.setVar('SSTATE_INSTDIR', sstateinst, d)
    bb.data.setVar('SSTATE_PKG', sstatepkg, d)
    bb.build.exec_func('sstate_unpack_package', d)

    # Fixup hardcoded paths
    fixmefn =  sstateinst + "fixmepath"
    if os.path.isfile(fixmefn):
        staging = bb.data.getVar('STAGING_DIR', d, True)
        fixmefd = open(fixmefn, "r")
        fixmefiles = fixmefd.readlines()
        fixmefd.close()
        for file in fixmefiles:
            os.system("sed -i -e s:FIXMESTAGINGDIR:%s:g %s" % (staging, sstateinst + file))

    for state in ss['dirs']:
        if os.path.exists(state[1]):
            oe.path.remove(state[1])
        oe.path.copytree(sstateinst + state[0], state[1])
    sstate_install(ss, d)

    for plain in ss['plaindirs']:
        workdir = d.getVar('WORKDIR', True)
        src = sstateinst + "/" + plain.replace(workdir, '')
        dest = plain
        bb.mkdirhier(src)
        bb.mkdirhier(dest)
        oe.path.copytree(src, dest)

    return True

def sstate_clean_cachefile(ss, d):
    import oe.path

    sstatepkgdir = bb.data.getVar('SSTATE_DIR', d, True)
    sstatepkgfile = sstatepkgdir + '/' + bb.data.getVar('SSTATE_PKGSPEC', d, True) + "*_" + ss['name'] + ".tgz*"
    bb.note("Removing %s" % sstatepkgfile)
    oe.path.remove(sstatepkgfile)

def sstate_clean_cachefiles(d):
    for task in (bb.data.getVar('SSTATETASKS', d, True) or "").split():
        ss = sstate_state_fromvars(d, task[3:])
        sstate_clean_cachefile(ss, d)

def sstate_clean_manifest(manifest, d):
    import oe.path

    if not os.path.exists(manifest):
       return

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

    manifest = bb.data.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'], d)

    locks = []
    for lock in ss['lockfiles']:
        locks.append(bb.utils.lockfile(lock))

    sstate_clean_manifest(manifest, d)

    for lock in locks:
        bb.utils.unlockfile(lock)

SCENEFUNCS += "sstate_cleanall"
CLEANFUNCS += "sstate_cleanall"

python sstate_cleanall() {
    import fnmatch

    bb.note("Removing shared state for package %s" % bb.data.getVar('PN', d, True))

    manifest_dir = bb.data.getVar('SSTATE_MANIFESTS', d, True)
    manifest_prefix = bb.data.getVar("SSTATE_MANFILEPREFIX", d, True)
    manifest_pattern = os.path.basename(manifest_prefix) + ".*"

    if not os.path.exists(manifest_dir):
        return

    for manifest in (os.listdir(manifest_dir)):
        if fnmatch.fnmatch(manifest, manifest_pattern):
             name = manifest.replace(manifest_pattern[:-1], "")
             namemap = d.getVar('SSTATETASKNAMES', True).split()
             tasks = d.getVar('SSTATETASKS', True).split()
             taskname = tasks[namemap.index(name)]
             shared_state = sstate_state_fromvars(d, taskname[3:])
             sstate_clean(shared_state, d)
}

def sstate_package(ss, d):
    import oe.path

    sstatebuild = bb.data.expand("${WORKDIR}/sstate-build-%s/" % ss['name'], d)
    sstatepkg = bb.data.getVar('SSTATE_PKG', d, True) + '_'+ ss['name'] + ".tgz"
    bb.mkdirhier(sstatebuild)
    bb.mkdirhier(os.path.dirname(sstatepkg))
    for state in ss['dirs']:
        srcbase = state[0].rstrip("/").rsplit('/', 1)[0]
        oe.path.copytree(state[1], sstatebuild + state[0])
        for walkroot, dirs, files in os.walk(state[1]):
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], sstatebuild + state[0])
                bb.debug(2, "Preparing %s for packaging at %s" % (srcpath, dstpath))

    workdir = bb.data.getVar('WORKDIR', d, True)
    for plain in ss['plaindirs']:
        pdir = plain.replace(workdir, sstatebuild)
        bb.mkdirhier(plain)
        bb.mkdirhier(pdir)
        oe.path.copytree(plain, pdir)

    bb.data.setVar('SSTATE_BUILDDIR', sstatebuild, d)
    bb.data.setVar('SSTATE_PKG', sstatepkg, d)
    bb.build.exec_func('sstate_create_package', d)
    
    bb.siggen.dump_this_task(sstatepkg + ".siginfo", d)

    return

def pstaging_fetch(sstatepkg, d):
    import bb.fetch

    # only try and fetch if the user has configured a mirror

    mirrors = bb.data.getVar('SSTATE_MIRRORS', d, True)
    if mirrors:
        # Copy the data object and override DL_DIR and SRC_URI
        localdata = bb.data.createCopy(d)
        bb.data.update_data(localdata)

        dldir = bb.data.expand("${SSTATE_DIR}", localdata)
        srcuri = "file://" + os.path.basename(sstatepkg)

        bb.mkdirhier(dldir)

        bb.data.setVar('DL_DIR', dldir, localdata)
        bb.data.setVar('PREMIRRORS', mirrors, localdata)
        bb.data.setVar('SRC_URI', srcuri, localdata)

        # Try a fetch from the sstate mirror, if it fails just return and
        # we will build the package
        try:
            bb.fetch.init([srcuri], localdata)
            bb.fetch.go(localdata, [srcuri])
            # Need to optimise this, if using file:// urls, the fetcher just changes the local path
            # For now work around by symlinking
            localpath = bb.data.expand(bb.fetch.localpath(srcuri, localdata), localdata)
            if localpath != sstatepkg and os.path.exists(localpath):
                os.symlink(localpath, sstatepkg)
        except:
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
	# Need to remove hardcoded paths and fix these when we install the
	# staging packages.
	for i in `${SSTATE_SCAN_CMD}` ; do \
		sed -i -e s:${STAGING_DIR}:FIXMESTAGINGDIR:g $i
		echo $i | sed -e 's:${SSTATE_BUILDDIR}::' >> ${SSTATE_BUILDDIR}fixmepath
	done

	cd ${SSTATE_BUILDDIR}
	tar -cvzf ${SSTATE_PKG} *

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
        "do_package_write_ipk" : "deploy-ipk",
        "do_package_write_deb" : "deploy-deb",
        "do_package_write_rpm" : "deploy-rpm",
        "do_package" : "package",
        "do_deploy" : "deploy",
    }

    for task in range(len(sq_fn)):
        sstatefile = bb.data.expand("${SSTATE_DIR}/" + sq_hashfn[task] + "_" + mapping[sq_task[task]] + ".tgz", d)
        sstatefile = sstatefile.replace("${BB_TASKHASH}", sq_hash[task])
        #print("Checking for %s" % sstatefile)
        if os.path.exists(sstatefile):
            ret.append(task)
            continue

    mirrors = bb.data.getVar("SSTATE_MIRRORS", d, True)
    if mirrors:
        # Copy the data object and override DL_DIR and SRC_URI
        localdata = bb.data.createCopy(d)
        bb.data.update_data(localdata)

        dldir = bb.data.expand("${SSTATE_DIR}", localdata)
        bb.data.setVar('DL_DIR', dldir, localdata)
        bb.data.setVar('PREMIRRORS', mirrors, localdata)

        for task in range(len(sq_fn)):
            if task in ret:
                continue

            sstatefile = bb.data.expand("${SSTATE_DIR}/" + sq_hashfn[task] + "_" + mapping[sq_task[task]] + ".tgz", d)
            sstatefile = sstatefile.replace("${BB_TASKHASH}", sq_hash[task])

            srcuri = "file://" + os.path.basename(sstatefile)
            bb.data.setVar('SRC_URI', srcuri, localdata)
            #bb.note(str(srcuri))

            try:
                bb.fetch.init(srcuri.split(), localdata)
                bb.fetch.checkstatus(localdata, srcuri.split())
                ret.append(task)
            except:
                pass     

    return ret

