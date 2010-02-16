SYSROOT_PREPROCESS_FUNCS += "relocatable_binaries_preprocess"

CHRPATH_BIN ?= "chrpath"
PREPROCESS_RELOCATE_DIRS ?= ""

def rpath_replace (path, d):
    import subprocess as sub

    cmd = bb.data.expand('${CHRPATH_BIN}', d)

    bindirs = bb.data.expand("${bindir} ${sbindir} ${base_sbindir} ${base_bindir} ${PREPROCESS_RELOCATE_DIRS}", d).split()
    tmpdir = bb.data.getVar('TMPDIR', d)
    basedir = bb.data.expand('${base_prefix}', d)

    for d in bindirs:
        dir = path + "/" + d
        bb.note("Checking %s for binaries to process" % dir)
        if os.path.exists(dir):
            for file in os.listdir(dir):
                fpath = dir + "/" + file
                #bb.note("Testing %s for relocatability" % fpath)
                p = sub.Popen([cmd, '-l', fpath],stdout=sub.PIPE,stderr=sub.PIPE)
                err, out = p.communicate()
                # If returned succesfully, process stderr for results
                if p.returncode == 0:
                    # Throw away everything other than the rpath list
                    curr_rpath = err.partition("RPATH=")[2]
                    #bb.note("Current rpath for %s is %s" % (fpath, curr_rpath.strip()))
                    rpaths = curr_rpath.split(":")
                    new_rpaths = []
                    for rpath in rpaths:
                        # If rpath is already dynamic continue
                        if rpath.find("$ORIGIN") != -1:
                            continue
                        # If the rpath shares a root with base_prefix determine a new dynamic rpath from the
                        # base_prefix shared root
                        if rpath.find(basedir) != -1:
                            depth = fpath.partition(basedir)[2].count('/')
                            libpath = rpath.partition(basedir)[2].strip()
                        # otherwise (i.e. cross packages) determine a shared root based on the TMPDIR
                        # NOTE: This will not work reliably for cross packages, particularly in the case
                        # where your TMPDIR is a short path (i.e. /usr/poky) as chrpath cannot insert an
                        # rpath longer than that which is already set.
                        else:
                            depth = fpath.rpartition(tmpdir)[2].count('/')
                            libpath = rpath.partition(tmpdir)[2].strip()

                        base = "$ORIGIN"
                        while depth > 1:
                            base += "/.."
                            depth-=1
                        new_rpaths.append("%s%s" % (base, libpath))

                    # if we have modified some rpaths call chrpath to update the binary
                    if len(new_rpaths):
                        args = ":".join(new_rpaths)
                        #bb.note("Setting rpath to " + args)
                        sub.call([cmd, '-r', args, fpath])

python relocatable_binaries_preprocess() {
    rpath_replace(bb.data.expand('${SYSROOT_DESTDIR}', d), d)
}
