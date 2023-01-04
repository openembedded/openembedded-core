#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import codecs
import os

def packaged(pkg, d):
    return os.access(get_subpkgedata_fn(pkg, d) + '.packaged', os.R_OK)

def read_pkgdatafile(fn):
    pkgdata = {}

    def decode(str):
        c = codecs.getdecoder("unicode_escape")
        return c(str)[0]

    if os.access(fn, os.R_OK):
        import re
        with open(fn, 'r') as f:
            lines = f.readlines()
        r = re.compile(r"(^.+?):\s+(.*)")
        for l in lines:
            m = r.match(l)
            if m:
                pkgdata[m.group(1)] = decode(m.group(2))

    return pkgdata

def get_subpkgedata_fn(pkg, d):
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
# Collapse FOO:pkg variables into FOO
#
def read_subpkgdata_dict(pkg, d):
    ret = {}
    subd = read_pkgdatafile(get_subpkgedata_fn(pkg, d))
    for var in subd:
        newvar = var.replace(":" + pkg, "")
        if newvar == var and var + ":" + pkg in subd:
            continue
        ret[newvar] = subd[var]
    return ret

def read_subpkgdata_extended(pkg, d):
    import json
    import bb.compress.zstd

    fn = d.expand("${PKGDATA_DIR}/extended/%s.json.zstd" % pkg)
    try:
        num_threads = int(d.getVar("BB_NUMBER_THREADS"))
        with bb.compress.zstd.open(fn, "rt", encoding="utf-8", num_threads=num_threads) as f:
            return json.load(f)
    except FileNotFoundError:
        return None

def _pkgmap(d):
    """Return a dictionary mapping package to recipe name."""

    pkgdatadir = d.getVar("PKGDATA_DIR")

    pkgmap = {}
    try:
        files = os.listdir(pkgdatadir)
    except OSError:
        bb.warn("No files in %s?" % pkgdatadir)
        files = []

    for pn in [f for f in files if not os.path.isdir(os.path.join(pkgdatadir, f))]:
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

def get_package_mapping(pkg, basepkg, d, depversions=None):
    import oe.packagedata

    data = oe.packagedata.read_subpkgdata(pkg, d)
    key = "PKG:%s" % pkg

    if key in data:
        if bb.data.inherits_class('allarch', d) and bb.data.inherits_class('packagegroup', d) and pkg != data[key]:
            bb.error("An allarch packagegroup shouldn't depend on packages which are dynamically renamed (%s to %s)" % (pkg, data[key]))
        # Have to avoid undoing the write_extra_pkgs(global_variants...)
        if bb.data.inherits_class('allarch', d) and not d.getVar('MULTILIB_VARIANTS') \
            and data[key] == basepkg:
            return pkg
        if depversions == []:
            # Avoid returning a mapping if the renamed package rprovides its original name
            rprovkey = "RPROVIDES:%s" % pkg
            if rprovkey in data:
                if pkg in bb.utils.explode_dep_versions2(data[rprovkey]):
                    bb.note("%s rprovides %s, not replacing the latter" % (data[key], pkg))
                    return pkg
        # Do map to rewritten package name
        return data[key]

    return pkg

def get_package_additional_metadata(pkg_type, d):
    base_key = "PACKAGE_ADD_METADATA"
    for key in ("%s_%s" % (base_key, pkg_type.upper()), base_key):
        if d.getVar(key, False) is None:
            continue
        d.setVarFlag(key, "type", "list")
        if d.getVarFlag(key, "separator") is None:
            d.setVarFlag(key, "separator", "\\n")
        metadata_fields = [field.strip() for field in oe.data.typed_value(key, d)]
        return "\n".join(metadata_fields).strip()

def runtime_mapping_rename(varname, pkg, d):
    #bb.note("%s before: %s" % (varname, d.getVar(varname)))

    new_depends = {}
    deps = bb.utils.explode_dep_versions2(d.getVar(varname) or "")
    for depend, depversions in deps.items():
        new_depend = get_package_mapping(depend, pkg, d, depversions)
        if depend != new_depend:
            bb.note("package name mapping done: %s -> %s" % (depend, new_depend))
        new_depends[new_depend] = deps[depend]

    d.setVar(varname, bb.utils.join_deps(new_depends, commasep=False))

    #bb.note("%s after: %s" % (varname, d.getVar(varname)))


