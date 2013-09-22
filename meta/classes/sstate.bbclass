SSTATE_VERSION = "3"

SSTATE_MANIFESTS ?= "${TMPDIR}/sstate-control"
SSTATE_MANFILEPREFIX = "${SSTATE_MANIFESTS}/manifest-${SSTATE_MANMACH}-${PN}"

def generate_sstatefn(spec, hash, d):
    if not hash:
        hash = "INVALID"
    return hash[:2] + "/" + spec + hash

SSTATE_PKGARCH    = "${PACKAGE_ARCH}"
SSTATE_PKGSPEC    = "sstate-${PN}-${PACKAGE_ARCH}${TARGET_VENDOR}-${TARGET_OS}-${PV}-${PR}-${SSTATE_PKGARCH}-${SSTATE_VERSION}-"
SSTATE_PKGNAME    = "${SSTATE_EXTRAPATH}${@generate_sstatefn(d.getVar('SSTATE_PKGSPEC', True), d.getVar('BB_TASKHASH', True), d)}"
SSTATE_PKG        = "${SSTATE_DIR}/${SSTATE_PKGNAME}"
SSTATE_EXTRAPATH   = ""
SSTATE_EXTRAPATHWILDCARD = ""
SSTATE_PATHSPEC   = "${SSTATE_DIR}/${SSTATE_EXTRAPATHWILDCARD}*/${SSTATE_PKGSPEC}"

SSTATE_DUPWHITELIST = "${DEPLOY_DIR_IMAGE}/ ${DEPLOY_DIR}/licenses/"
# Also need to make cross recipes append to ${PN} and install once for any given PACAGE_ARCH so
# can avoid multiple installs (e.g. routerstationpro+qemumips both using mips32)
SSTATE_DUPWHITELIST += "${STAGING_LIBDIR_NATIVE}/${MULTIMACH_TARGET_SYS} ${STAGING_DIR_NATIVE}/usr/libexec/${MULTIMACH_TARGET_SYS} ${STAGING_BINDIR_NATIVE}/${MULTIMACH_TARGET_SYS} ${STAGING_DIR_NATIVE}${includedir_native}/gcc-build-internal-${MULTIMACH_TARGET_SYS}"
# Avoid docbook/sgml catalog warnings for now
SSTATE_DUPWHITELIST += "${STAGING_ETCDIR_NATIVE}/sgml ${STAGING_DATADIR_NATIVE}/sgml"

SSTATE_SCAN_FILES ?= "*.la *-config *_config"
SSTATE_SCAN_CMD ?= 'find ${SSTATE_BUILDDIR} \( -name "${@"\" -o -name \"".join(d.getVar("SSTATE_SCAN_FILES", True).split())}" \) -type f'

BB_HASHFILENAME = "${SSTATE_EXTRAPATH} ${SSTATE_PKGSPEC}"

SSTATE_MANMACH ?= "${SSTATE_PKGARCH}"

SSTATEPREINSTFUNCS ?= ""
SSTATEPOSTINSTFUNCS ?= ""
EXTRA_STAGING_FIXMES ?= ""

# Specify dirs in which the shell function is executed and don't use ${B}
# as default dirs to avoid possible race about ${B} with other task.
sstate_create_package[dirs] = "${SSTATE_BUILDDIR}"
sstate_unpack_package[dirs] = "${SSTATE_INSTDIR}"

python () {
    if bb.data.inherits_class('native', d):
        d.setVar('SSTATE_PKGARCH', d.getVar('BUILD_ARCH'))
    elif bb.data.inherits_class('crosssdk', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${BUILD_ARCH}_${SDK_ARCH}"))
    elif bb.data.inherits_class('cross', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${BUILD_ARCH}_${TUNE_PKGARCH}"))
        d.setVar('SSTATE_MANMACH', d.expand("${BUILD_ARCH}_${MACHINE}"))
    elif bb.data.inherits_class('nativesdk', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${SDK_ARCH}"))
    elif bb.data.inherits_class('cross-canadian', d):
        d.setVar('SSTATE_PKGARCH', d.expand("${SDK_ARCH}_${PACKAGE_ARCH}"))
    elif bb.data.inherits_class('allarch', d) and d.getVar("PACKAGE_ARCH", True) == "all":
        d.setVar('SSTATE_PKGARCH', "allarch")
    else:
        d.setVar('SSTATE_MANMACH', d.expand("${PACKAGE_ARCH}"))

    if bb.data.inherits_class('native', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('cross', d):
        d.setVar('SSTATE_EXTRAPATH', "${NATIVELSBSTRING}/")
        d.setVar('SSTATE_EXTRAPATHWILDCARD', "*/")

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
    if not source.endswith("/"):
         source = source + "/"
    if not dest.endswith("/"):
         dest = dest + "/"
    source = os.path.normpath(source)
    dest = os.path.normpath(dest)
    srcbase = os.path.basename(source)
    ss['dirs'].append([srcbase, source, dest])
    return ss

def sstate_install(ss, d):
    import oe.path
    import subprocess

    sharedfiles = []
    shareddirs = []
    bb.utils.mkdirhier(d.expand("${SSTATE_MANIFESTS}"))

    d2 = d.createCopy()
    extrainf = d.getVarFlag("do_" + ss['task'], 'stamp-extra-info', True)
    if extrainf:
        d2.setVar("SSTATE_MANMACH", extrainf)
    manifest = d2.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'])

    if os.access(manifest, os.R_OK):
        bb.fatal("Package already staged (%s)?!" % manifest)

    locks = []
    for lock in ss['lockfiles-shared']:
        locks.append(bb.utils.lockfile(lock, True))
    for lock in ss['lockfiles']:
        locks.append(bb.utils.lockfile(lock))

    for state in ss['dirs']:
        bb.debug(2, "Staging files from %s to %s" % (state[1], state[2]))
        for walkroot, dirs, files in os.walk(state[1]):
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

    # Check the file list for conflicts against files which already exist
    whitelist = (d.getVar("SSTATE_DUPWHITELIST", True) or "").split()
    match = []
    for f in sharedfiles:
        if os.path.exists(f):
            f = os.path.normpath(f)
            realmatch = True
            for w in whitelist:
                if f.startswith(w):
                    realmatch = False
                    break
            if realmatch:
                match.append(f)
                sstate_search_cmd = "grep -rl '%s' %s --exclude=master.list | sed -e 's:^.*/::' -e 's:\.populate-sysroot::'" % (f, d.expand("${SSTATE_MANIFESTS}"))
                search_output = subprocess.Popen(sstate_search_cmd, shell=True, stdout=subprocess.PIPE).communicate()[0]
                if search_output != "":
                    match.append("Matched in %s" % search_output.rstrip())
    if match:
        bb.warn("The recipe %s is trying to install files into a shared area when those files already exist. Those files and their manifest location are:\n   %s\nPlease verify which package should provide the above files." % (d.getVar('PN', True), "\n   ".join(match)))

    # Write out the manifest
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

    # Run the actual file install
    for state in ss['dirs']:
        if os.path.exists(state[1]):
            oe.path.copyhardlinktree(state[1], state[2])

    for postinst in (d.getVar('SSTATEPOSTINSTFUNCS', True) or '').split():
        bb.build.exec_func(postinst, d)

    for lock in locks:
        bb.utils.unlockfile(lock)

def sstate_installpkg(ss, d):
    import oe.path
    import subprocess

    def prepdir(dir):
        # remove dir if it exists, ensure any parent directories do exist
        if os.path.exists(dir):
            oe.path.remove(dir)
        bb.utils.mkdirhier(dir)
        oe.path.remove(dir)

    sstateinst = d.expand("${WORKDIR}/sstate-install-%s/" % ss['name'])
    sstatefetch = d.getVar('SSTATE_PKGNAME', True) + '_' + ss['name'] + ".tgz"
    sstatepkg = d.getVar('SSTATE_PKG', True) + '_' + ss['name'] + ".tgz"

    if not os.path.exists(sstatepkg):
        pstaging_fetch(sstatefetch, sstatepkg, d)

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
    #
    # Note: The logic below must match the reverse logic in
    # sstate_hardcode_path(d)

    fixmefn =  sstateinst + "fixmepath"
    if os.path.isfile(fixmefn):
        staging = d.getVar('STAGING_DIR', True)
        staging_target = d.getVar('STAGING_DIR_TARGET', True)
        staging_host = d.getVar('STAGING_DIR_HOST', True)

        if bb.data.inherits_class('native', d) or bb.data.inherits_class('nativesdk', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('cross-canadian', d):
            sstate_sed_cmd = "sed -i -e 's:FIXMESTAGINGDIR:%s:g'" % (staging)
        elif bb.data.inherits_class('cross', d):
            sstate_sed_cmd = "sed -i -e 's:FIXMESTAGINGDIRTARGET:%s:g; s:FIXMESTAGINGDIR:%s:g'" % (staging_target, staging)
        else:
            sstate_sed_cmd = "sed -i -e 's:FIXMESTAGINGDIRHOST:%s:g'" % (staging_host)

        extra_staging_fixmes = d.getVar('EXTRA_STAGING_FIXMES', True) or ''
        for fixmevar in extra_staging_fixmes.split():
            fixme_path = d.getVar(fixmevar, True)
            sstate_sed_cmd += " -e 's:FIXME_%s:%s:g'" % (fixmevar, fixme_path)

        # Add sstateinst to each filename in fixmepath, use xargs to efficiently call sed
        sstate_hardcode_cmd = "sed -e 's:^:%s:g' %s | xargs %s" % (sstateinst, fixmefn, sstate_sed_cmd)

        bb.note("Replacing fixme paths in sstate package: %s" % (sstate_hardcode_cmd))
        subprocess.call(sstate_hardcode_cmd, shell=True)

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
        bb.utils.mkdirhier(src)
        prepdir(dest)
        os.rename(src, dest)

    return True

def sstate_clean_cachefile(ss, d):
    import oe.path

    sstatepkgfile = d.getVar('SSTATE_PATHSPEC', True) + "*_" + ss['name'] + ".tgz*"
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

    d2 = d.createCopy()
    extrainf = d.getVarFlag("do_" + ss['task'], 'stamp-extra-info', True)
    if extrainf:
        d2.setVar("SSTATE_MANMACH", extrainf)
    manifest = d2.expand("${SSTATE_MANFILEPREFIX}.%s" % ss['name'])

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
    bb.note("Removing shared state for package %s" % d.getVar('PN', True))

    manifest_dir = d.getVar('SSTATE_MANIFESTS', True)
    if not os.path.exists(manifest_dir):
        return

    namemap = d.getVar('SSTATETASKNAMES', True).split()
    tasks = d.getVar('SSTATETASKS', True).split()
    for name in namemap:
        taskname = tasks[namemap.index(name)]
        shared_state = sstate_state_fromvars(d, taskname[3:])
        sstate_clean(shared_state, d)
}

def sstate_hardcode_path(d):
    import subprocess, platform

    # Need to remove hardcoded paths and fix these when we install the
    # staging packages.
    #
    # Note: the logic in this function needs to match the reverse logic
    # in sstate_installpkg(ss, d)

    staging = d.getVar('STAGING_DIR', True)
    staging_target = d.getVar('STAGING_DIR_TARGET', True)
    staging_host = d.getVar('STAGING_DIR_HOST', True)
    sstate_builddir = d.getVar('SSTATE_BUILDDIR', True)

    if bb.data.inherits_class('native', d) or bb.data.inherits_class('nativesdk', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('cross-canadian', d):
        sstate_grep_cmd = "grep -l -e '%s'" % (staging)
        sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIR:g'" % (staging)
    elif bb.data.inherits_class('cross', d):
        sstate_grep_cmd = "grep -l -e '(%s|%s)'" % (staging_target, staging)
        sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIRTARGET:g; s:%s:FIXMESTAGINGDIR:g'" % (staging_target, staging)
    else:
        sstate_grep_cmd = "grep -l -e '%s'" % (staging_host)
        sstate_sed_cmd = "sed -i -e 's:%s:FIXMESTAGINGDIRHOST:g'" % (staging_host)

    extra_staging_fixmes = d.getVar('EXTRA_STAGING_FIXMES', True) or ''
    for fixmevar in extra_staging_fixmes.split():
        fixme_path = d.getVar(fixmevar, True)
        sstate_sed_cmd += " -e 's:%s:FIXME_%s:g'" % (fixme_path, fixmevar)

    fixmefn =  sstate_builddir + "fixmepath"

    sstate_scan_cmd = d.getVar('SSTATE_SCAN_CMD', True)
    sstate_filelist_cmd = "tee %s" % (fixmefn)

    # fixmepath file needs relative paths, drop sstate_builddir prefix
    sstate_filelist_relative_cmd = "sed -i -e 's:^%s::g' %s" % (sstate_builddir, fixmefn)

    xargs_no_empty_run_cmd = '--no-run-if-empty'
    if platform.system() == 'Darwin':
        xargs_no_empty_run_cmd = ''

    # Limit the fixpaths and sed operations based on the initial grep search
    # This has the side effect of making sure the vfs cache is hot
    sstate_hardcode_cmd = "%s | xargs %s | %s | xargs %s %s" % (sstate_scan_cmd, sstate_grep_cmd, sstate_filelist_cmd, xargs_no_empty_run_cmd, sstate_sed_cmd)

    bb.note("Removing hardcoded paths from sstate package: '%s'" % (sstate_hardcode_cmd))
    subprocess.call(sstate_hardcode_cmd, shell=True)

        # If the fixmefn is empty, remove it..
    if os.stat(fixmefn).st_size == 0:
        os.remove(fixmefn)
    else:
        bb.note("Replacing absolute paths in fixmepath file: '%s'" % (sstate_filelist_relative_cmd))
        subprocess.call(sstate_filelist_relative_cmd, shell=True)

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

        depth = outputpath.rpartition(tmpdir)[2].count('/')
        base = link.partition(tmpdir)[2].strip()
        while depth > 1:
            base = "/.." + base
            depth -= 1
        base = "." + base

        bb.debug(2, "Replacing absolute path %s with relative path %s for %s" % (link, base, outputpath))
        os.remove(path)
        os.symlink(base, path)

    tmpdir = d.getVar('TMPDIR', True)

    sstatebuild = d.expand("${WORKDIR}/sstate-build-%s/" % ss['name'])
    sstatepkg = d.getVar('SSTATE_PKG', True) + '_'+ ss['name'] + ".tgz"
    bb.utils.remove(sstatebuild, recurse=True)
    bb.utils.mkdirhier(sstatebuild)
    bb.utils.mkdirhier(os.path.dirname(sstatepkg))
    for state in ss['dirs']:
        if not os.path.exists(state[1]):
            continue
        srcbase = state[0].rstrip("/").rsplit('/', 1)[0]
        for walkroot, dirs, files in os.walk(state[1]):
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], state[2])
                make_relative_symlink(srcpath, dstpath, d)
            for dir in dirs:
                srcpath = os.path.join(walkroot, dir)
                dstpath = srcpath.replace(state[1], state[2])
                make_relative_symlink(srcpath, dstpath, d)
        bb.debug(2, "Preparing tree %s for packaging at %s" % (state[1], sstatebuild + state[0]))
        oe.path.copyhardlinktree(state[1], sstatebuild + state[0])

    workdir = d.getVar('WORKDIR', True)
    for plain in ss['plaindirs']:
        pdir = plain.replace(workdir, sstatebuild)
        bb.utils.mkdirhier(plain)
        bb.utils.mkdirhier(pdir)
        oe.path.copyhardlinktree(plain, pdir)

    d.setVar('SSTATE_BUILDDIR', sstatebuild)
    d.setVar('SSTATE_PKG', sstatepkg)
    sstate_hardcode_path(d)
    bb.build.exec_func('sstate_create_package', d)
    
    bb.siggen.dump_this_task(sstatepkg + ".siginfo", d)

    return

def pstaging_fetch(sstatefetch, sstatepkg, d):
    import bb.fetch2

    # Only try and fetch if the user has configured a mirror
    mirrors = d.getVar('SSTATE_MIRRORS', True)
    if not mirrors:
        return

    # Copy the data object and override DL_DIR and SRC_URI
    localdata = bb.data.createCopy(d)
    bb.data.update_data(localdata)

    dldir = localdata.expand("${SSTATE_DIR}")
    bb.utils.mkdirhier(dldir)

    localdata.delVar('MIRRORS')
    localdata.delVar('FILESPATH')
    localdata.setVar('DL_DIR', dldir)
    localdata.setVar('PREMIRRORS', mirrors)

    # if BB_NO_NETWORK is set but we also have SSTATE_MIRROR_ALLOW_NETWORK,
    # we'll want to allow network access for the current set of fetches.
    if localdata.getVar('BB_NO_NETWORK', True) == "1" and localdata.getVar('SSTATE_MIRROR_ALLOW_NETWORK', True) == "1":
        localdata.delVar('BB_NO_NETWORK')

    # Try a fetch from the sstate mirror, if it fails just return and
    # we will build the package
    for srcuri in ['file://{0}'.format(sstatefetch),
                   'file://{0}.siginfo'.format(sstatefetch)]:
        localdata.setVar('SRC_URI', srcuri)
        try:
            fetcher = bb.fetch2.Fetch([srcuri], localdata, cache=False)
            fetcher.download()

            # Need to optimise this, if using file:// urls, the fetcher just changes the local path
            # For now work around by symlinking
            localpath = bb.data.expand(fetcher.localpath(srcuri), localdata)
            if localpath != sstatepkg and os.path.exists(localpath) and not os.path.exists(sstatepkg):
                os.symlink(localpath, sstatepkg)

        except bb.fetch2.BBFetchException:
            break

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
    omask = os.umask(002)
    if omask != 002:
       bb.note("Using umask 002 (not %0o) for sstate packaging" % omask)
    sstate_package(shared_state, d)
    os.umask(omask)
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
		set +e
		tar -czf $TFILE *
		if [ $? -ne 0 ] && [ $? -ne 1 ]; then
			exit 1
		fi
		set -e
	else
		tar -cz --file=$TFILE --files-from=/dev/null
	fi
	chmod 0664 $TFILE 
	mv -f $TFILE ${SSTATE_PKG}

	cd ${WORKDIR}
	rm -rf ${SSTATE_BUILDDIR}
}

#
# Shell function to decompress and prepare a package for installation
#
sstate_unpack_package () {
	mkdir -p ${SSTATE_INSTDIR}
	cd ${SSTATE_INSTDIR}
	tar -xmvzf ${SSTATE_PKG}
}

# Need to inject information about classes not in the global configuration scope
EXTRASSTATEMAPS += "do_deploy:deploy"

BB_HASHCHECK_FUNCTION = "sstate_checkhashes"

def sstate_checkhashes(sq_fn, sq_task, sq_hash, sq_hashfn, d):

    ret = []
    mapping = {}
    for t in d.getVar("SSTATETASKS", True).split():
        mapping[t] = d.getVarFlag(t, "sstate-name", True)
    for extra in d.getVar("EXTRASSTATEMAPS", True).split():
        e = extra.split(":")
        mapping[e[0]] = e[1]

    for task in range(len(sq_fn)):
        spec = sq_hashfn[task].split(" ")[1]
        extrapath = sq_hashfn[task].split(" ")[0]

        sstatefile = d.expand("${SSTATE_DIR}/" + extrapath + generate_sstatefn(spec, sq_hash[task], d) + "_" + mapping[sq_task[task]] + ".tgz")
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

        # if BB_NO_NETWORK is set but we also have SSTATE_MIRROR_ALLOW_NETWORK,
        # we'll want to allow network access for the current set of fetches.
        if localdata.getVar('BB_NO_NETWORK', True) == "1" and localdata.getVar('SSTATE_MIRROR_ALLOW_NETWORK', True) == "1":
            localdata.delVar('BB_NO_NETWORK')

        for task in range(len(sq_fn)):
            if task in ret:
                continue

            spec = sq_hashfn[task].split(" ")[1]
            extrapath = sq_hashfn[task].split(" ")[0]
            sstatefile = d.expand(extrapath + generate_sstatefn(spec, sq_hash[task], d) + "_" + mapping[sq_task[task]] + ".tgz")

            srcuri = "file://" + sstatefile
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

BB_SETSCENE_DEPVALID = "setscene_depvalid"

def setscene_depvalid(task, taskdependees, notneeded, d):
    # taskdependees is a dict of tasks which depend on task, each being a 3 item list of [PN, TASKNAME, FILENAME]
    # task is included in taskdependees too

    bb.debug(2, "Considering setscene task: %s" % (str(taskdependees[task])))

    def isNative(x):
        return x.endswith("-native")
    def isNativeCross(x):
        return x.endswith("-native") or x.endswith("-cross") or x.endswith("-cross-initial")
    def isSafeDep(x):
        if x in ["quilt-native", "autoconf-native", "automake-native", "gnu-config-native", "libtool-native", "pkgconfig-native", "gcc-cross", "binutils-cross", "gcc-cross-initial"]:
            return True
        return False
    def isPostInstDep(x):
        if x in ["qemu-native", "gdk-pixbuf-native", "qemuwrapper-cross", "depmodwrapper-cross", "systemd-systemctl-native", "gtk-update-icon-cache-native"]:
            return True
        return False

    # We can skip these "safe" dependencies since the aren't runtime dependencies, just build time
    if isSafeDep(taskdependees[task][0]) and taskdependees[task][1] == "do_populate_sysroot":
        return True

    # We only need to trigger populate_lic through direct dependencies
    if taskdependees[task][1] == "do_populate_lic":
        return True

    for dep in taskdependees:
        bb.debug(2, "  considering dependency: %s" % (str(taskdependees[dep])))
        if task == dep:
            continue
        if dep in notneeded:
            continue
        # do_package_write_* and do_package doesn't need do_package
        if taskdependees[task][1] == "do_package" and taskdependees[dep][1] in ['do_package', 'do_package_write_deb', 'do_package_write_ipk', 'do_package_write_rpm', 'do_packagedata']:
            continue
        # do_package_write_* and do_package doesn't need do_populate_sysroot, unless is a postinstall dependency
        if taskdependees[task][1] == "do_populate_sysroot" and taskdependees[dep][1] in ['do_package', 'do_package_write_deb', 'do_package_write_ipk', 'do_package_write_rpm', 'do_packagedata']:
            if isPostInstDep(taskdependees[task][0]) and taskdependees[dep][1] in ['do_package_write_deb', 'do_package_write_ipk', 'do_package_write_rpm']:
                return False
            continue
        # Native/Cross packages don't exist and are noexec anyway
        if isNativeCross(taskdependees[dep][0]) and taskdependees[dep][1] in ['do_package_write_deb', 'do_package_write_ipk', 'do_package_write_rpm', 'do_packagedata']:
            continue

        # Consider sysroot depending on sysroot tasks
        if taskdependees[task][1] == 'do_populate_sysroot' and taskdependees[dep][1] == 'do_populate_sysroot':
            # base-passwd/shadow-sysroot don't need their dependencies
            if taskdependees[dep][0].endswith(("base-passwd", "shadow-sysroot")):
                continue
            # Nothing need depend on libc-initial/gcc-cross-initial
            if taskdependees[task][0].endswith("-initial"):
                continue
            # Native/Cross populate_sysroot need their dependencies
            if isNativeCross(taskdependees[task][0]) and isNativeCross(taskdependees[dep][0]):
                return False
            # Target populate_sysroot depended on by cross tools need to be installed
            if isNativeCross(taskdependees[dep][0]):
                return False
            # Native/cross tools depended upon by target sysroot are not needed
            if isNativeCross(taskdependees[task][0]):
                continue
            # Target populate_sysroot need their dependencies
            return False

        # This is due to the [depends] in useradd.bbclass complicating matters
        # The logic *is* reversed here due to the way hard setscene dependencies are injected
        if taskdependees[task][1] == 'do_package' and taskdependees[dep][0].endswith(('shadow-native', 'shadow-sysroot', 'base-passwd', 'pseudo-native')) and taskdependees[dep][1] == 'do_populate_sysroot':
            continue

        # Safe fallthrough default
        bb.debug(2, " Default setscene dependency fall through due to dependency: %s" % (str(taskdependees[dep])))
        return False
    return True

