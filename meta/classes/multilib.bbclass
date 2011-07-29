python multilib_virtclass_handler () {
    if not isinstance(e, bb.event.RecipePreFinalise):
        return

    cls = e.data.getVar("BBEXTENDCURR", True)
    variant = e.data.getVar("BBEXTENDVARIANT", True)
    if cls != "multilib" or not variant:
        return
    save_var_name=e.data.getVar("MULTILIB_SAVE_VARNAME", True) or ""
    for name in save_var_name.split():
        val=e.data.getVar(name, True)
        if val:
            e.data.setVar(name + "_MULTILIB_ORIGINAL", val)

    override = ":virtclass-multilib-" + variant

    e.data.setVar("MLPREFIX", variant + "-")
    e.data.setVar("PN", variant + "-" + e.data.getVar("PN", False))
    e.data.setVar("SHLIBSDIR_virtclass-multilib-" + variant ,e.data.getVar("SHLIBSDIR", False) + "/" + variant)
    e.data.setVar("TARGET_VENDOR_virtclass-multilib-" + variant, e.data.getVar("TARGET_VENDOR", False) + "ml" + variant)
    e.data.setVar("OVERRIDES", e.data.getVar("OVERRIDES", False) + override)
}

addhandler multilib_virtclass_handler

STAGINGCC_prepend = "${BBEXTENDVARIANT}-"

python __anonymous () {
    variant = d.getVar("BBEXTENDVARIANT", True)

    def extend_name(name):
        if name.startswith("virtual/"):
            subs = name.split("/", 1)[1]
            if not subs.startswith(variant):
                return "virtual/" + variant + "-" + subs
            return name
        if not name.startswith(variant):
            return variant + "-" + name
        return name

    def map_dependencies(varname, d, suffix = ""):
        if suffix:
            varname = varname + "_" + suffix
        deps = d.getVar(varname, True)
        if not deps:
            return
        deps = bb.utils.explode_deps(deps)
        newdeps = []
        for dep in deps:
            if dep.endswith(("-native", "-native-runtime")):
                newdeps.append(dep)
            else:
                newdeps.append(extend_name(dep))
        d.setVar(varname, " ".join(newdeps))

    def map_variable(varname, d):
        var = d.getVar(varname, True)
        if not var:
            return
        var = var.split()
        newvar = []
        for v in var:
            newvar.append(extend_name(v))
        d.setVar(varname, " ".join(newvar))

    pkgs = []
    pkgrename = {}
    for pkg in (d.getVar("PACKAGES", True) or "").split():
        if pkg.startswith(variant):
            pkgs.append(pkg)
            continue
        pkgrename[pkg] = extend_name(pkg)
        pkgs.append(pkgrename[pkg])

    if pkgrename:
        d.setVar("PACKAGES", " ".join(pkgs))
        for pkg in pkgrename:
            for subs in ["FILES", "RDEPENDS", "RRECOMMENDS", "SUMMARY", "DESCRIPTION", "RSUGGESTS", "RPROVIDES", "RCONFLICTS", "PKG", "ALLOW_EMPTY"]:
                d.renameVar("%s_%s" % (subs, pkg), "%s_%s" % (subs, pkgrename[pkg]))

    map_dependencies("DEPENDS", d)
    for pkg in (d.getVar("PACKAGES", True).split() + [""]):
        map_dependencies("RDEPENDS", d, pkg)
        map_dependencies("RRECOMMENDS", d, pkg)
        map_dependencies("RSUGGESTS", d, pkg)
        map_dependencies("RPROVIDES", d, pkg)
        map_dependencies("RREPLACES", d, pkg)
        map_dependencies("RCONFLICTS", d, pkg)
        map_dependencies("PKG", d, pkg)

    map_variable("PROVIDES", d)
    map_variable("PACKAGES_DYNAMIC", d)
    map_variable("PACKAGE_INSTALL", d)
}
