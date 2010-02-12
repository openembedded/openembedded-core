SYSROOT_PREPROCESS_FUNCS += "relocatable_binaries_preprocess"

CHRPATH_BIN ?= "chrpath"

def rpath_replace (path, d):
    import subprocess as sub

    cmd = bb.data.expand('${CHRPATH_BIN}', d)
    tmpdir = bb.data.expand('${base_prefix}', d)

    for root, dirs, files in os.walk(path):
        for file in files:
            fpath = root + '/' + file
            if '/bin/' in fpath:
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
                        depth = fpath.partition(tmpdir)[2].strip().count('/')
                        if depth == 3:
                            # / is two levels up
                            root = "$ORIGIN/../.."
                        else:
                            root = "$ORIGIN/.."

                        # kill everything up to "/"
                        new_rpaths.append("%s%s" % (root, rpath.partition(tmpdir)[2].strip()))
                    args = ":".join(new_rpaths)
                    #bb.note("Setting rpath to " + args)
                    sub.call([cmd, '-r', args, fpath])

python relocatable_binaries_preprocess() {
    rpath_replace(bb.data.expand("${SYSROOT_DESTDIR}${TMPDIR}/sysroots/${TARGET_ARCH}-${TARGET_OS}", d), d)
}
