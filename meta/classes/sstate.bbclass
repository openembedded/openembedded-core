PSTAGE2_MANIFESTS = "${TMPDIR}/pstagelogs"
PSTAGE2_MANFILEPREFIX = "${PSTAGE2_MANIFESTS}/manifest-${PSTAGE2_PKGARCH}-${PN}"


PSTAGE2_PKGARCH    = "${BASE_PACKAGE_ARCH}"
PSTAGE2_PKGVERSION = "${PV}-${PR}"
PSTAGE2_PKGPN      = "${@bb.data.expand('staging-${PN}-${MULTIMACH_ARCH}${TARGET_VENDOR}-${TARGET_OS}', d).replace('_', '-')}"

PSTAGE2_PKGNAME    = "${PSTAGE2_PKGPN}_${PSTAGE2_PKGVERSION}_${PSTAGE2_PKGARCH}"
PSTAGE2_EXTRAPATH  ?= ""
PSTAGE2_PKGPATH    = "${DISTRO}/${OELAYOUT_ABI}${PSTAGE2_EXTRAPATH}"
PSTAGE2_PKG        = "${PSTAGE_DIR}2/${PSTAGE2_PKGPATH}/${PSTAGE2_PKGNAME}"

PSTAGE2_SCAN_CMD ?= "find ${PSTAGE2_BUILDDIR} \( -name "*.la" -o -name "*-config" \) -type f"

python () {
    if bb.data.inherits_class('native', d):
        bb.data.setVar('PSTAGE2_PKGARCH', bb.data.getVar('BUILD_ARCH', d), d)
    elif bb.data.inherits_class('cross', d) or bb.data.inherits_class('crosssdk', d):
        bb.data.setVar('PSTAGE2_PKGARCH', bb.data.expand("${BUILD_ARCH}_${BASE_PACKAGE_ARCH}", d), d)
    elif bb.data.inherits_class('nativesdk', d):
        bb.data.setVar('PSTAGE2_PKGARCH', bb.data.expand("${SDK_ARCH}", d), d)
    elif bb.data.inherits_class('cross-canadian', d):
        bb.data.setVar('PSTAGE2_PKGARCH', bb.data.expand("${SDK_ARCH}_${BASE_PACKAGE_ARCH}", d), d)

    # These classes encode staging paths into their scripts data so can only be
    # reused if we manipulate the paths
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d) or bb.data.inherits_class('crosssdk', d):
        scan_cmd = "grep -Irl ${STAGING_DIR} ${PSTAGE2_BUILDDIR}"
        bb.data.setVar('PSTAGE2_SCAN_CMD', scan_cmd, d)

    for task in (bb.data.getVar('SSTATETASKS', d, True) or "").split():
        funcs = bb.data.getVarFlag(task, 'prefuncs', d) or ""
        funcs = "sstate_task_prefunc " + funcs
        bb.data.setVarFlag(task, 'prefuncs', funcs, d)
        funcs = bb.data.getVarFlag(task, 'postfuncs', d) or ""
        funcs = "sstate_task_postfunc " + funcs
        bb.data.setVarFlag(task, 'postfuncs', funcs, d)
}

def sstate_init(name, d):
    ss = {}
    ss['name'] = name
    ss['dirs'] = []
    ss['plaindirs'] = []
    ss['lockfiles'] = []
    return ss

def sstate_state_fromvars(d):
    task = bb.data.getVar('BB_CURRENTTASK', d, True)
    if not task:
        bb.fatal("sstate code running without task context?!")
    task = task.replace("_setscene", "")

    name = bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-name', d), d)
    inputs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-inputdirs', d) or "", d)).split()
    outputs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-outputdirs', d) or "", d)).split()
    plaindirs = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-plaindirs', d) or "", d)).split()
    lockfiles = (bb.data.expand(bb.data.getVarFlag("do_" + task, 'sstate-lockfile', d) or "", d)).split()
    if not name or len(inputs) != len(outputs):
        bb.fatal("sstate variables not setup correctly?!")

    ss = sstate_init(name, d)
    for i in range(len(inputs)):
        sstate_add(ss, inputs[i], outputs[i], d)
    ss['lockfiles'] = lockfiles
    ss['plaindirs'] = plaindirs
    return ss

def sstate_add(ss, source, dest, d):
    srcbase = os.path.basename(source)
    ss['dirs'].append([srcbase, source, dest])
    return ss

def sstate_install(ss, d):
    import oe.path

    sharedfiles = []
    shareddirs = []
    bb.mkdirhier(bb.data.expand("${PSTAGE2_MANIFESTS}", d))
    manifest = bb.data.expand("${PSTAGE2_MANFILEPREFIX}.%s" % ss['name'], d)

    if os.access(manifest, os.R_OK):
        bb.fatal("Package already staged (%s)?!" % manifest)

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
                dir = os.path.join(state[2], dir)
                if not dir.endswith("/"):
                    dir = dir + "/"
                shareddirs.append(dir)
    f = open(manifest, "w")
    for file in sharedfiles:
        f.write(file + "\n")
    # We want to ensure that directories appear at the end of the manifest
    # so that when we test to see if they should be deleted any contents
    # added by the task will have been removed first.
    for dir in shareddirs:
        f.write(dir + "\n")
    f.close()

    for lock in locks:
        bb.utils.unlockfile(lock)

def sstate_installpkg(ss, d):
    import oe.path

    pstageinst = bb.data.expand("${WORKDIR}/pstage-install-%s/" % ss['name'], d)
    pstagepkg = bb.data.getVar('PSTAGE2_PKG', d, True) + '_' + ss['name'] + ".tgz"

    if not os.path.exists(pstagepkg):
       pstaging_fetch(pstagepkg, d)

    if not os.path.isfile(pstagepkg):
        bb.note("Staging package %s does not exist" % pstagepkg)
        return False

    sstate_clean(ss, d)

    bb.data.setVar('PSTAGE2_INSTDIR', pstageinst, d)
    bb.data.setVar('PSTAGE2_PKG', pstagepkg, d)
    bb.build.exec_func('sstate_unpack_package', d)

    # Fixup hardcoded paths
    fixmefn =  pstageinst + "fixmepath"
    if os.path.isfile(fixmefn):
        staging = bb.data.getVar('STAGING_DIR', d, True)
        fixmefd = open(fixmefn, "r")
        fixmefiles = fixmefd.readlines()
        fixmefd.close()
        for file in fixmefiles:
            os.system("sed -i -e s:FIXMESTAGINGDIR:%s:g %s" % (staging, pstageinst + file))

    for state in ss['dirs']:
        if os.path.exists(state[1]):
            oe.path.remove(state[1])
        oe.path.copytree(pstageinst + state[0], state[1])
    sstate_install(ss, d)

    for plain in ss['plaindirs']:
        bb.mkdirhier(pstageinst + plain)
        oe.path.copytree(pstageinst + plain, bb.data.getVar('WORKDIR', d, True) + plain)

    return True

def sstate_clean_manifest(manifest, d):
    import oe.path

    if not os.path.exists(manifest):
       return

    mfile = open(manifest)
    entries = mfile.readlines()
    mfile.close()

    for entry in entries:
        entry = entry.strip()
        if entry.endswith("/"):
           if os.path.exists(entry) and len(os.listdir(entry)) == 0:
              os.rmdir(entry)
        else:
           oe.path.remove(entry)

    oe.path.remove(manifest)

def sstate_clean(ss, d):

    manifest = bb.data.expand("${PSTAGE2_MANFILEPREFIX}.%s" % ss['name'], d)

    locks = []
    for lock in ss['lockfiles']:
        locks.append(bb.utils.lockfile(lock))

    sstate_clean_manifest(manifest, d)

    for lock in locks:
        bb.utils.unlockfile(lock)

python sstate_cleanall() {
    import fnmatch

    bb.note("Removing %s from staging" % bb.data.getVar('PN', d, True))

    manifest_dir = bb.data.getVar('PSTAGE2_MANIFESTS', d, True)
    manifest_pattern = bb.data.expand("manifest-${PN}.*", d)

    for manifest in (os.listdir(manifest_dir)):
        if fnmatch.fnmatch(manifest, manifest_pattern):
             sstate_clean_manifest(manifest_dir + "/" + manifest, d)
}

do_clean[postfuncs] += "sstate_cleanall"
do_clean[dirs] += "${PSTAGE2_MANIFESTS}"

def sstate_package(ss, d):
    import oe.path

    pstagebuild = bb.data.expand("${WORKDIR}/pstage-build-%s/" % ss['name'], d)
    pstagepkg = bb.data.getVar('PSTAGE2_PKG', d, True) + '_'+ ss['name'] + ".tgz"
    bb.mkdirhier(pstagebuild)
    bb.mkdirhier(os.path.dirname(pstagepkg))
    for state in ss['dirs']:
        srcbase = state[0].rstrip("/").rsplit('/', 1)[0]
        oe.path.copytree(state[1], pstagebuild + state[0])
        for walkroot, dirs, files in os.walk(state[1]):
            for file in files:
                srcpath = os.path.join(walkroot, file)
                dstpath = srcpath.replace(state[1], pstagebuild + state[0])
                bb.debug(2, "Preparing %s for packaging at %s" % (srcpath, dstpath))

    workdir = bb.data.getVar('WORKDIR', d, True)
    for plain in ss['plaindirs']:
        pdir = plain.replace(workdir, pstagebuild)
        bb.mkdirhier(plain)
        bb.mkdirhier(pdir)
        oe.path.copytree(plain, pdir)

    bb.data.setVar('PSTAGE2_BUILDDIR', pstagebuild, d)
    bb.data.setVar('PSTAGE2_PKG', pstagepkg, d)
    bb.build.exec_func('sstate_create_package', d)

    return

def pstaging_fetch(pstagepkg, d):
    import bb.fetch

    # only try and fetch if the user has configured a mirror
    if bb.data.getVar('PSTAGE_MIRROR', d) != "":
        # Copy the data object and override DL_DIR and SRC_URI
        pd = d.createCopy()
        dldir = bb.data.expand("${PSTAGE_DIR}/${PSTAGE_PKGPATH}", pd)
        mirror = bb.data.expand("${PSTAGE_MIRROR}/${PSTAGE_PKGPATH}/", pd)
        srcuri = mirror + os.path.basename(pstagepkg)
        bb.data.setVar('DL_DIR', dldir, pd)
        bb.data.setVar('SRC_URI', srcuri, pd)

        # Try a fetch from the pstage mirror, if it fails just return and
        # we will build the package
        try:
            bb.fetch.init([srcuri], pd)
            bb.fetch.go(pd, [srcuri])
        except:
            return

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
    sstate_package(shared_state, d)
}
  

#
# Shell function to generate a pstage package from a directory
# set as PSTAGE2_BUILDDIR
#
sstate_create_package () {
	# Need to remove hardcoded paths and fix these when we install the
	# staging packages.
	for i in `${PSTAGE2_SCAN_CMD}` ; do \
		sed -i -e s:${STAGING_DIR}:FIXMESTAGINGDIR:g $i
		echo $i | sed -e 's:${PSTAGE2_BUILDDIR}::' >> ${PSTAGE2_BUILDDIR}fixmepath
	done

	cd ${PSTAGE2_BUILDDIR}
	tar -cvzf ${PSTAGE2_PKG} *
}

#
# Shell function to decompress and prepare a package for installation
#
sstate_unpack_package () {
	mkdir -p ${PSTAGE2_INSTDIR}
	cd ${PSTAGE2_INSTDIR}
	tar -xvzf ${PSTAGE2_PKG}
}
