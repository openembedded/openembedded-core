import itertools

def is_optional(group, d):
    return bool(d.getVarFlag("PACKAGE_GROUP_%s" % group, "optional"))

def packages(groups, d):
    for group in groups:
        for pkg in (d.getVar("PACKAGE_GROUP_%s" % group, True) or "").split():
            yield pkg

def required_packages(groups, d):
    req = filter(lambda group: not is_optional(group, d), groups)
    return packages(req, d)

def optional_packages(groups, d):
    opt = filter(lambda group: is_optional(group, d), groups)
    return packages(opt, d)

def active_packages(features, d):
    return itertools.chain(required_packages(features, d),
                           optional_packages(features, d))

def active_recipes(features, d):
    import oe.packagedata

    for pkg in active_packages(features, d):
        recipe = oe.packagedata.recipename(pkg, d)
        if recipe:
            yield recipe
