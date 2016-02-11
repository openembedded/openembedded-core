NATIVELSBSTRING = "universal"

UNINATIVE_LOADER ?= "${STAGING_DIR}-uninative/${BUILD_ARCH}-linux/lib/${@bb.utils.contains('BUILD_ARCH', 'x86_64', 'ld-linux-x86-64.so.2', 'ld-linux.so.2', d)}"

UNINATIVE_URL ?= "unset"
UNINATIVE_TARBALL ?= "${BUILD_ARCH}-nativesdk-libc.tar.bz2"
# Example checksums
#UNINATIVE_CHECKSUM[i586] = "dead"
#UNINATIVE_CHECKSUM[x86_64] = "dead"
UNINATIVE_DLDIR ?= "${COREBASE}"

addhandler uninative_eventhandler
uninative_eventhandler[eventmask] = "bb.event.BuildStarted"

python uninative_eventhandler() {
    loader = e.data.getVar("UNINATIVE_LOADER", True)
    tarball = d.getVar("UNINATIVE_TARBALL", True)
    tarballdir = d.getVar("UNINATIVE_DLDIR", True)
    tarballpath = os.path.join(tarballdir, tarball)

    if not os.path.exists(loader):
        import subprocess

        olddir = os.getcwd()
        if not os.path.exists(tarballpath):
            # Copy the data object and override DL_DIR and SRC_URI
            localdata = bb.data.createCopy(d)

            if d.getVar("UNINATIVE_URL", True) == "unset":
                bb.fatal("Uninative selected but not configured, please set UNINATIVE_URL")

            chksum = d.getVarFlag("UNINATIVE_CHECKSUM", d.getVar("BUILD_ARCH", True), True)
            if not chksum:
                bb.fatal("Uninative selected but not configured correctly, please set UNINATIVE_CHECKSUM[%s]" % d.getVar("BUILD_ARCH", True))

            srcuri = d.expand("${UNINATIVE_URL}${UNINATIVE_TARBALL};md5sum=%s" % chksum)
            localdata.setVar('FILESPATH', tarballdir)
            localdata.setVar('DL_DIR', tarballdir)
            bb.note("Fetching uninative binary shim from %s" % srcuri)
            fetcher = bb.fetch2.Fetch([srcuri], localdata, cache=False)
            try:
                fetcher.download()
                localpath = fetcher.localpath(srcuri)
                if localpath != tarballpath and os.path.exists(localpath) and not os.path.exists(tarballpath):
                    os.symlink(localpath, tarballpath)
            except Exception as exc:
                bb.fatal("Unable to download uninative tarball: %s" % str(exc))

        cmd = e.data.expand("mkdir -p ${STAGING_DIR}-uninative; cd ${STAGING_DIR}-uninative; tar -xjf ${UNINATIVE_DLDIR}/${UNINATIVE_TARBALL}; ${STAGING_DIR}-uninative/relocate_sdk.py ${STAGING_DIR}-uninative/${BUILD_ARCH}-linux ${UNINATIVE_LOADER} ${UNINATIVE_LOADER} ${STAGING_DIR}-uninative/${BUILD_ARCH}-linux/${bindir_native}/patchelf-uninative")
        try:
            subprocess.check_call(cmd, shell=True)
        except subprocess.CalledProcessError as exc:
            bb.fatal("Unable to install uninative tarball: %s" % str(exc))
        os.chdir(olddir)
}

SSTATEPOSTUNPACKFUNCS_append = " uninative_changeinterp"

PATH_prepend = "${STAGING_DIR}-uninative/${BUILD_ARCH}-linux${bindir_native}:"

python uninative_changeinterp () {
    import subprocess
    import stat
    import oe.qa

    if not (bb.data.inherits_class('native', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('cross', d)):
        return

    sstateinst = d.getVar('SSTATE_INSTDIR', True)
    for walkroot, dirs, files in os.walk(sstateinst):
        for file in files:
            if file.endswith(".so") or ".so." in file:
                continue
            f = os.path.join(walkroot, file)
            if os.path.islink(f):
                continue
            s = os.stat(f)
            if not ((s[stat.ST_MODE] & stat.S_IXUSR) or (s[stat.ST_MODE] & stat.S_IXGRP) or (s[stat.ST_MODE] & stat.S_IXOTH)):
                continue
            elf = oe.qa.ELFFile(f)
            try:
                elf.open()
            except:
                continue

            #bb.warn("patchelf-uninative --set-interpreter %s %s" % (d.getVar("UNINATIVE_LOADER", True), f))
            cmd = "patchelf-uninative --set-interpreter %s %s" % (d.getVar("UNINATIVE_LOADER", True), f)
            p = subprocess.Popen(cmd, shell=True,
                                 stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            stdout, stderr = p.communicate()
            if p.returncode:
                bb.fatal("'%s' failed with exit code %d and the following output:\n%s" %
                         (cmd, p.returncode, stdout))
}
