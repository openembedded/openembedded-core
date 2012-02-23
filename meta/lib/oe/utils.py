import bb, bb.data

def read_file(filename):
    try:
        f = file( filename, "r" )
    except IOError, reason:
        return "" # WARNING: can't raise an error now because of the new RDEPENDS handling. This is a bit ugly. :M:
    else:
        return f.read().strip()
    return None

def ifelse(condition, iftrue = True, iffalse = False):
    if condition:
        return iftrue
    else:
        return iffalse

def conditional(variable, checkvalue, truevalue, falsevalue, d):
    if d.getVar(variable,1) == checkvalue:
        return truevalue
    else:
        return falsevalue

def less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    if float(d.getVar(variable,1)) <= float(checkvalue):
        return truevalue
    else:
        return falsevalue

def version_less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    result = bb.utils.vercmp_string(d.getVar(variable,True), checkvalue)
    if result <= 0:
        return truevalue
    else:
        return falsevalue

def contains(variable, checkvalues, truevalue, falsevalue, d):
    val = d.getVar(variable, True)
    if not val:
        return falsevalue
    val = set(val.split())
    if isinstance(checkvalues, basestring):
        checkvalues = set(checkvalues.split())
    else:
        checkvalues = set(checkvalues)
    if checkvalues.issubset(val):
        return truevalue
    return falsevalue

def both_contain(variable1, variable2, checkvalue, d):
    if d.getVar(variable1,1).find(checkvalue) != -1 and d.getVar(variable2,1).find(checkvalue) != -1:
        return checkvalue
    else:
        return ""

def prune_suffix(var, suffixes, d):
    # See if var ends with any of the suffixes listed and 
    # remove it if found
    for suffix in suffixes:
        if var.endswith(suffix):
            var = var.replace(suffix, "")

    prefix = d.getVar("MLPREFIX", True)
    if prefix and var.startswith(prefix):
        var = var.replace(prefix, "")

    return var

def str_filter(f, str, d):
    from re import match
    return " ".join(filter(lambda x: match(f, x, 0), str.split()))

def str_filter_out(f, str, d):
    from re import match
    return " ".join(filter(lambda x: not match(f, x, 0), str.split()))

def param_bool(cfg, field, dflt = None):
    """Lookup <field> in <cfg> map and convert it to a boolean; take
    <dflt> when this <field> does not exist"""
    value = cfg.get(field, dflt)
    strvalue = str(value).lower()
    if strvalue in ('yes', 'y', 'true', 't', '1'):
        return True
    elif strvalue in ('no', 'n', 'false', 'f', '0'):
        return False
    raise ValueError("invalid value for boolean parameter '%s': '%s'" % (field, value))

def inherits(d, *classes):
    """Return True if the metadata inherits any of the specified classes"""
    return any(bb.data.inherits_class(cls, d) for cls in classes)

def distro_features_backfill(d):
    # This construct allows the addition of new features to DISTRO_FEATURES
    # that if not present would disable existing functionality, without
    # disturbing distributions that have already set DISTRO_FEATURES.
    # Distributions wanting to elide a value in DISTRO_FEATURES_BACKFILL should
    # add the feature to DISTRO_FEATURES_BACKFILL_CONSIDERED

    backfill = (d.getVar("DISTRO_FEATURES_BACKFILL", True) or "").split()
    considered = (d.getVar("DISTRO_FEATURES_BACKFILL_CONSIDERED", True) or "").split()

    addfeatures = []
    for feature in backfill:
        if feature not in considered:
            addfeatures.append(feature)

    if addfeatures:
        return " %s" % (" ".join(addfeatures))
    else:
        return ""
