import os
import bb.data
import codecs

def packaged(pkg, d):
    return os.access(get_subpkgedata_fn(pkg, d) + '.packaged', os.R_OK)

def read_pkgdatafile(fn):
    pkgdata = {}

    def decode(str):
        c = codecs.getdecoder("string_escape")
        return c(str)[0]

    if os.access(fn, os.R_OK):
        import re
        f = file(fn, 'r')
        lines = f.readlines()
        f.close()
        r = re.compile("([^:]+):\s*(.*)")
        for l in lines:
            m = r.match(l)
            if m:
                pkgdata[m.group(1)] = decode(m.group(2))

    return pkgdata

def get_subpkgedata_fn(pkg, d):
    archs = d.expand("${PACKAGE_ARCHS}").split(" ")
    archs.reverse()
    pkgdata = d.expand('${TMPDIR}/pkgdata/')
    targetdir = d.expand('${TARGET_VENDOR}-${TARGET_OS}/runtime/')
    for arch in archs:
        fn = pkgdata + arch + targetdir + pkg
        if os.path.exists(fn):
            return fn
    return d.expand('${PKGDATA_DIR}/runtime/%s' % pkg)

def has_subpkgdata(pkg, d):
    return os.access(get_subpkgedata_fn(pkg, d), os.R_OK)

def read_subpkgdata(pkg, d):
    return read_pkgdatafile(get_subpkgedata_fn(pkg, d))

def has_pkgdata(pn, d):
    fn = d.expand('${PKGDATA_DIR}/%s' % pn)
    return os.access(fn, os.R_OK)

def read_pkgdata(pn, d):
    fn = d.expand('${PKGDATA_DIR}/%s' % pn)
    return read_pkgdatafile(fn)

#
# Collapse FOO_pkg variables into FOO
#
def read_subpkgdata_dict(pkg, d):
    ret = {}
    subd = read_pkgdatafile(get_subpkgedata_fn(pkg, d))
    for var in subd:
        newvar = var.replace("_" + pkg, "")
        if newvar == var and var + "_" + pkg in subd:
            continue
        ret[newvar] = subd[var]
    return ret

def _pkgmap(d):
    """Return a dictionary mapping package to recipe name."""

    target_os = d.getVar("TARGET_OS", True)
    target_vendor = d.getVar("TARGET_VENDOR", True)
    basedir = os.path.dirname(d.getVar("PKGDATA_DIR", True))

    dirs = ("%s%s-%s" % (arch, target_vendor, target_os)
            for arch in d.getVar("PACKAGE_ARCHS", True).split())

    pkgmap = {}
    for pkgdatadir in (os.path.join(basedir, sys) for sys in dirs):
        try:
            files = os.listdir(pkgdatadir)
        except OSError:
            continue

        for pn in filter(lambda f: not os.path.isdir(os.path.join(pkgdatadir, f)), files):
            try:
                pkgdata = read_pkgdatafile(os.path.join(pkgdatadir, pn))
            except OSError:
                continue

            packages = pkgdata.get("PACKAGES") or ""
            for pkg in packages.split():
                pkgmap[pkg] = pn

    return pkgmap

def pkgmap(d):
    """Return a dictionary mapping package to recipe name.
    Cache the mapping in the metadata"""

    pkgmap_data = d.getVar("__pkgmap_data", False)
    if pkgmap_data is None:
        pkgmap_data = _pkgmap(d)
        d.setVar("__pkgmap_data", pkgmap_data)

    return pkgmap_data

def recipename(pkg, d):
    """Return the recipe name for the given binary package name."""

    return pkgmap(d).get(pkg)
