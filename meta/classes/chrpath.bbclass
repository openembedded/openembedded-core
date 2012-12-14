CHRPATH_BIN ?= "chrpath"
PREPROCESS_RELOCATE_DIRS ?= ""

def process_dir (directory, d):
    import subprocess as sub
    import stat

    cmd = d.expand('${CHRPATH_BIN}')
    tmpdir = os.path.normpath(d.getVar('TMPDIR'))
    basedir = os.path.normpath(d.expand('${base_prefix}'))

    #bb.debug("Checking %s for binaries to process" % directory)
    if not os.path.exists(directory):
        return

    dirs = os.listdir(directory)
    for file in dirs:
        fpath = directory + "/" + file
        fpath = os.path.normpath(fpath)
        if os.path.islink(fpath):
            # Skip symlinks
            continue

        if os.path.isdir(fpath):
            process_dir(fpath, d)
        else:
            #bb.note("Testing %s for relocatability" % fpath)

            # We need read and write permissions for chrpath, if we don't have
            # them then set them temporarily. Take a copy of the files
            # permissions so that we can restore them afterwards.
            perms = os.stat(fpath)[stat.ST_MODE]
            if os.access(fpath, os.W_OK|os.R_OK):
                perms = None
            else:
                # Temporarily make the file writeable so we can chrpath it
                os.chmod(fpath, perms|stat.S_IRWXU)

            p = sub.Popen([cmd, '-l', fpath],stdout=sub.PIPE,stderr=sub.PIPE)
            err, out = p.communicate()
            # If returned succesfully, process stderr for results
            if p.returncode != 0:
                continue

            # Throw away everything other than the rpath list
            curr_rpath = err.partition("RPATH=")[2]
            #bb.note("Current rpath for %s is %s" % (fpath, curr_rpath.strip()))
            rpaths = curr_rpath.split(":")
            new_rpaths = []
            for rpath in rpaths:
                # If rpath is already dynamic copy it to new_rpath and continue
                if rpath.find("$ORIGIN") != -1:
                    new_rpaths.append(rpath.strip())
                    continue
                rpath =  os.path.normpath(rpath)
                # If the rpath shares a root with base_prefix determine a new dynamic rpath from the
                # base_prefix shared root
                if rpath.find(basedir) != -1:
                    depth = fpath.partition(basedir)[2].count('/')
                    libpath = rpath.partition(basedir)[2].strip()
                # otherwise (i.e. cross packages) determine a shared root based on the TMPDIR
                # NOTE: This will not work reliably for cross packages, particularly in the case
                # where your TMPDIR is a short path (i.e. /usr/poky) as chrpath cannot insert an
                # rpath longer than that which is already set.
                elif rpath.find(tmpdir) != -1:
                    depth = fpath.rpartition(tmpdir)[2].count('/')
                    libpath = rpath.partition(tmpdir)[2].strip()
                else:
                    new_rpaths.append(rpath.strip())
                    continue
                base = "$ORIGIN"
                while depth > 1:
                    base += "/.."
                    depth-=1
                new_rpaths.append("%s%s" % (base, libpath))

            # if we have modified some rpaths call chrpath to update the binary
            if len(new_rpaths):
                args = ":".join(new_rpaths)
                #bb.note("Setting rpath for %s to %s" %(fpath, args))
                p = sub.Popen([cmd, '-r', args, fpath],stdout=sub.PIPE,stderr=sub.PIPE)
                out, err = p.communicate()
                if p.returncode != 0:
                    bb.error("%s: chrpath command failed with exit code %d:\n%s%s" % (d.getVar('PN', True), p.returncode, out, err))
                    raise bb.build.FuncFailed

            if perms:
                os.chmod(fpath, perms)

def rpath_replace (path, d):
    bindirs = d.expand("${bindir} ${sbindir} ${base_sbindir} ${base_bindir} ${libdir} ${base_libdir} ${libexecdir} ${PREPROCESS_RELOCATE_DIRS}").split()

    for bindir in bindirs:
        #bb.note ("Processing directory " + bindir)
        directory = path + "/" + bindir
        process_dir (directory, d)

