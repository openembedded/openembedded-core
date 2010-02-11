SYSROOT_PREPROCESS_FUNCS += "relocatable_binaries_preprocess"

CHRPATH_BIN ?= "chrpath"

def rpath_replace (paths, d):
    chrpath = bb.data.expand('${CHRPATH_BIN}', d)

    for path in paths:
        for root, dirs, files in os.walk(path):
            for f in files:
                if 'usr' in path:
                    os.system("%s -r $ORIGIN/../lib:$ORIGIN/../../lib %s/%s" % (chrpath, path,f))
                else:
                    os.system("%s -r $ORIGIN/../lib %s/%s" % (chrpath, path, f))

python relocatable_binaries_preprocess() {
    paths = []
    target = bb.data.expand("${SYSROOT_DESTDIR}${TMPDIR}/sysroots/${TARGET_ARCH}-${TARGET_OS}", d)

    paths.append(target + "/bin")
    paths.append(target + "/usr/bin")

    rpath_replace(paths, d)
}
