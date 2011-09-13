python multilib_virtclass_handler_global () {
    if not e.data:
        return

    variant = e.data.getVar("BBEXTENDVARIANT", True)

    if isinstance(e, bb.event.RecipeParsed) and not variant:
        if bb.data.inherits_class('kernel', e.data) or bb.data.inherits_class('module-base', e.data) or bb.data.inherits_class('allarch', e.data):
            origprovs = provs = e.data.getVar("PROVIDES", True)
            rprovs = e.data.getVar("RPROVIDES", True)
            variants = (e.data.getVar("MULTILIB_VARIANTS", True) or "").split()
            for variant in variants:
                provs = provs + " " + multilib_map_variable("PROVIDES", variant, e.data)
                for pkg in e.data.getVar("PACKAGES", True).split():
                    rprovs = rprovs + " " + variant + "-" + pkg
                e.data.setVar("PROVIDES", origprovs)
            e.data.setVar("PROVIDES", provs)
            e.data.setVar("RPROVIDES", rprovs)
}

addhandler multilib_virtclass_handler_global

def multilib_extend_name(variant, name):
    if name.startswith("kernel-module"):
        return name
    if name.startswith("virtual/"):
        subs = name.split("/", 1)[1]
        if not subs.startswith(variant):
            return "virtual/" + variant + "-" + subs
        return name
    if not name.startswith(variant):
        return variant + "-" + name
    return name

def multilib_map_variable(varname, variant, d):
    var = d.getVar(varname, True)
    if not var:
        return
    var = var.split()
    newvar = []
    for v in var:
        newvar.append(multilib_extend_name(variant, v))
    newdata =  " ".join(newvar)
    d.setVar(varname, newdata)
    return newdata
