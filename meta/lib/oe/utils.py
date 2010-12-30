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
    if bb.data.getVar(variable,d,1) == checkvalue:
        return truevalue
    else:
        return falsevalue

def less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    if float(bb.data.getVar(variable,d,1)) <= float(checkvalue):
        return truevalue
    else:
        return falsevalue

def version_less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    result = bb.vercmp(bb.data.getVar(variable,d,True), checkvalue)
    if result <= 0:
        return truevalue
    else:
        return falsevalue

def contains(variable, checkvalues, truevalue, falsevalue, d):
    val = bb.data.getVar(variable,d,1)
    if not val:
        return falsevalue
    matches = 0
    if type(checkvalues).__name__ == "str":
        checkvalues = [checkvalues]
    for value in checkvalues:
        if val.find(value) != -1:
            matches = matches + 1
    if matches == len(checkvalues):
        return truevalue
    return falsevalue

def both_contain(variable1, variable2, checkvalue, d):
    if bb.data.getVar(variable1,d,1).find(checkvalue) != -1 and bb.data.getVar(variable2,d,1).find(checkvalue) != -1:
        return checkvalue
    else:
        return ""

def prune_suffix(var, suffixes, d):
    # See if var ends with any of the suffixes listed and 
    # remove it if found
    for suffix in suffixes:
        if var.endswith(suffix):
            return var.replace(suffix, "")
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
