python multilib_virtclass_handler_global () {
    if not e.data:
        return

    variant = e.data.getVar("BBEXTENDVARIANT", True)

    if isinstance(e, bb.event.RecipeParsed) and not variant:
        if bb.data.inherits_class('kernel', e.data) or bb.data.inherits_class('module-base', e.data) or bb.data.inherits_class('allarch', e.data):
            variants = (e.data.getVar("MULTILIB_VARIANTS", True) or "").split()

            # Process PROVIDES
            origprovs = provs = e.data.getVar("PROVIDES", True) or ""
            for variant in variants:
                provs = provs + " " + multilib_map_variable("PROVIDES", variant, e.data)
                # Reset to original value so next time around multilib_map_variable works properly
                e.data.setVar("PROVIDES", origprovs)
            e.data.setVar("PROVIDES", provs)

            # Process RPROVIDES
            origrprovs = rprovs = e.data.getVar("RPROVIDES", True) or ""
            for variant in variants:
                rprovs = rprovs + " " + multilib_map_variable("RPROVIDES", variant, e.data)
                # Reset to original value so next time around multilib_map_variable works properly
                e.data.setVar("RPROVIDES", origrprovs)
            e.data.setVar("RPROVIDES", rprovs)

	    # Process RPROVIDES_${PN}...
            for pkg in (e.data.getVar("PACKAGES", True) or "").split():
                origrprovs = rprovs = e.data.getVar("RPROVIDES_%s" % pkg, True) or ""
                for variant in variants:
                    rprovs = rprovs + " " + multilib_map_variable("RPROVIDES_%s" % pkg, variant, e.data)
                    rprovs = rprovs + " " + variant + "-" + pkg
                    # Reset to original value so next time around multilib_map_variable works properly
                    e.data.setVar("RPROVIDES_%s" % pkg, origrprovs)
                e.data.setVar("RPROVIDES_%s" % pkg, rprovs)
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
        return ""
    var = var.split()
    newvar = []
    for v in var:
        newvar.append(multilib_extend_name(variant, v))
    newdata =  " ".join(newvar)
    d.setVar(varname, newdata)
    return newdata
