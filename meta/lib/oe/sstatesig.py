import bb.siggen

def sstate_rundepfilter(siggen, fn, recipename, task, dep, depname, dataCache):
    # Return True if we should keep the dependency, False to drop it
    def isNative(x):
        return x.endswith("-native")
    def isCross(x):
        return x.endswith("-cross") or x.endswith("-cross-initial") or x.endswith("-cross-intermediate")
    def isNativeSDK(x):
        return x.startswith("nativesdk-")
    def isKernel(fn):
        inherits = " ".join(dataCache.inherits[fn])
        return inherits.find("/module-base.bbclass") != -1 or inherits.find("/linux-kernel-base.bbclass") != -1
    def isPackageGroup(fn):
        inherits = " ".join(dataCache.inherits[fn])
        return "/packagegroup.bbclass" in inherits
    def isImage(fn):
        return "/image.bbclass" in " ".join(dataCache.inherits[fn])

    # Always include our own inter-task dependencies
    if recipename == depname:
        return True

    # Quilt (patch application) changing isn't likely to affect anything
    excludelist = ['quilt-native', 'subversion-native', 'git-native']
    if depname in excludelist and recipename != depname:
        return False

    # Don't change native/cross/nativesdk recipe dependencies any further
    if isNative(recipename) or isCross(recipename) or isNativeSDK(recipename):
        return True

    # Only target packages beyond here

    # packagegroups are assumed to have well behaved names which don't change between architecures/tunes
    if isPackageGroup(fn):
        return False  

    # Exclude well defined machine specific configurations which don't change ABI
    if depname in siggen.abisaferecipes and not isImage(fn):
        return False

    # Exclude well defined recipe->dependency
    if "%s->%s" % (recipename, depname) in siggen.saferecipedeps:
        return False

    # Kernel modules are well namespaced. We don't want to depend on the kernel's checksum
    # if we're just doing an RRECOMMENDS_xxx = "kernel-module-*", not least because the checksum
    # is machine specific.
    # Therefore if we're not a kernel or a module recipe (inheriting the kernel classes)
    # and we reccomend a kernel-module, we exclude the dependency.
    depfn = dep.rsplit(".", 1)[0]
    if dataCache and isKernel(depfn) and not isKernel(fn):
        for pkg in dataCache.runrecs[fn]:
            if " ".join(dataCache.runrecs[fn][pkg]).find("kernel-module-") != -1:
                return False

    # Default to keep dependencies
    return True

class SignatureGeneratorOEBasic(bb.siggen.SignatureGeneratorBasic):
    name = "OEBasic"
    def init_rundepcheck(self, data):
        self.abisaferecipes = (data.getVar("SIGGEN_EXCLUDERECIPES_ABISAFE", True) or "").split()
        self.saferecipedeps = (data.getVar("SIGGEN_EXCLUDE_SAFE_RECIPE_DEPS", True) or "").split()
        pass
    def rundep_check(self, fn, recipename, task, dep, depname, dataCache = None):
        return sstate_rundepfilter(self, fn, recipename, task, dep, depname, dataCache)

class SignatureGeneratorOEBasicHash(bb.siggen.SignatureGeneratorBasicHash):
    name = "OEBasicHash"
    def init_rundepcheck(self, data):
        self.abisaferecipes = (data.getVar("SIGGEN_EXCLUDERECIPES_ABISAFE", True) or "").split()
        self.saferecipedeps = (data.getVar("SIGGEN_EXCLUDE_SAFE_RECIPE_DEPS", True) or "").split()
        pass
    def rundep_check(self, fn, recipename, task, dep, depname, dataCache = None):
        return sstate_rundepfilter(self, fn, recipename, task, dep, depname, dataCache)

# Insert these classes into siggen's namespace so it can see and select them
bb.siggen.SignatureGeneratorOEBasic = SignatureGeneratorOEBasic
bb.siggen.SignatureGeneratorOEBasicHash = SignatureGeneratorOEBasicHash


def find_siginfo(pn, taskname, taskhashlist, d):
    """ Find signature data files for comparison purposes """

    import fnmatch
    import glob

    if taskhashlist:
        hashfiles = {}

    if not taskname:
        # We have to derive pn and taskname
        key = pn
        splitit = key.split('.bb.')
        taskname = splitit[1]
        pn = os.path.basename(splitit[0]).split('_')[0]
        if key.startswith('virtual:native:'):
            pn = pn + '-native'

    if taskname in ['do_fetch', 'do_unpack', 'do_patch', 'do_populate_lic']:
        pn.replace("-native", "")

    filedates = {}

    # First search in stamps dir
    localdata = d.createCopy()
    localdata.setVar('MULTIMACH_TARGET_SYS', '*')
    localdata.setVar('PN', pn)
    localdata.setVar('PV', '*')
    localdata.setVar('PR', '*')
    localdata.setVar('EXTENDPE', '')
    stamp = localdata.getVar('STAMP', True)
    filespec = '%s.%s.sigdata.*' % (stamp, taskname)
    foundall = False
    import glob
    for fullpath in glob.glob(filespec):
        match = False
        if taskhashlist:
            for taskhash in taskhashlist:
                if fullpath.endswith('.%s' % taskhash):
                    hashfiles[taskhash] = fullpath
                    if len(hashfiles) == len(taskhashlist):
                        foundall = True
                        break
        else:
            filedates[fullpath] = os.stat(fullpath).st_mtime

    if not taskhashlist or (len(filedates) < 2 and not foundall):
        # That didn't work, look in sstate-cache
        hashes = taskhashlist or ['*']
        localdata = bb.data.createCopy(d)
        for hashval in hashes:
            localdata.setVar('PACKAGE_ARCH', '*')
            localdata.setVar('TARGET_VENDOR', '*')
            localdata.setVar('TARGET_OS', '*')
            localdata.setVar('PN', pn)
            localdata.setVar('PV', '*')
            localdata.setVar('PR', '*')
            localdata.setVar('BB_TASKHASH', hashval)
            if pn.endswith('-native') or pn.endswith('-crosssdk') or pn.endswith('-cross'):
                localdata.setVar('SSTATE_EXTRAPATH', "${NATIVELSBSTRING}/")
            sstatename = taskname[3:]
            filespec = '%s_%s.*.siginfo' % (localdata.getVar('SSTATE_PKG', True), sstatename)

            if hashval != '*':
                sstatedir = "%s/%s" % (d.getVar('SSTATE_DIR', True), hashval[:2])
            else:
                sstatedir = d.getVar('SSTATE_DIR', True)

            for root, dirs, files in os.walk(sstatedir):
                for fn in files:
                    fullpath = os.path.join(root, fn)
                    if fnmatch.fnmatch(fullpath, filespec):
                        if taskhashlist:
                            hashfiles[hashval] = fullpath
                        else:
                            filedates[fullpath] = os.stat(fullpath).st_mtime

    if taskhashlist:
        return hashfiles
    else:
        return filedates

bb.siggen.find_siginfo = find_siginfo
