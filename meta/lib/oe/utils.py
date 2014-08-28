try:
    # Python 2
    import commands as cmdstatus
except ImportError:
    # Python 3
    import subprocess as cmdstatus

def read_file(filename):
    try:
        f = open( filename, "r" )
    except IOError as reason:
        return "" # WARNING: can't raise an error now because of the new RDEPENDS handling. This is a bit ugly. :M:
    else:
        data = f.read().strip()
        f.close()
        return data
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

def features_backfill(var,d):
    # This construct allows the addition of new features to variable specified
    # as var
    # Example for var = "DISTRO_FEATURES"
    # This construct allows the addition of new features to DISTRO_FEATURES
    # that if not present would disable existing functionality, without
    # disturbing distributions that have already set DISTRO_FEATURES.
    # Distributions wanting to elide a value in DISTRO_FEATURES_BACKFILL should
    # add the feature to DISTRO_FEATURES_BACKFILL_CONSIDERED
    features = (d.getVar(var, True) or "").split()
    backfill = (d.getVar(var+"_BACKFILL", True) or "").split()
    considered = (d.getVar(var+"_BACKFILL_CONSIDERED", True) or "").split()

    addfeatures = []
    for feature in backfill:
        if feature not in features and feature not in considered:
            addfeatures.append(feature)

    if addfeatures:
        d.appendVar(var, " " + " ".join(addfeatures))


def packages_filter_out_system(d):
    """
    Return a list of packages from PACKAGES with the "system" packages such as
    PN-dbg PN-doc PN-locale-eb-gb removed.
    """
    pn = d.getVar('PN', True)
    blacklist = map(lambda suffix: pn + suffix, ('', '-dbg', '-dev', '-doc', '-locale', '-staticdev'))
    localepkg = pn + "-locale-"
    pkgs = []

    for pkg in d.getVar('PACKAGES', True).split():
        if pkg not in blacklist and localepkg not in pkg:
            pkgs.append(pkg)
    return pkgs

def getstatusoutput(cmd):
    return cmdstatus.getstatusoutput(cmd)


def trim_version(version, num_parts=2):
    """
    Return just the first <num_parts> of <version>, split by periods.  For
    example, trim_version("1.2.3", 2) will return "1.2".
    """
    if type(version) is not str:
        raise TypeError("Version should be a string")
    if num_parts < 1:
        raise ValueError("Cannot split to parts < 1")

    parts = version.split(".")
    trimmed = ".".join(parts[:num_parts])
    return trimmed

def cpu_count():
    import multiprocessing
    return multiprocessing.cpu_count()

def execute_pre_post_process(d, cmds):
    if cmds is None:
        return

    for cmd in cmds.strip().split(';'):
        cmd = cmd.strip()
        if cmd != '':
            bb.note("Executing %s ..." % cmd)
            bb.build.exec_func(cmd, d)

def multiprocess_exec(commands, function):
    import signal
    import multiprocessing

    if not commands:
        return []

    def init_worker():
        signal.signal(signal.SIGINT, signal.SIG_IGN)

    nproc = min(multiprocessing.cpu_count(), len(commands))
    pool = bb.utils.multiprocessingpool(nproc, init_worker)
    imap = pool.imap(function, commands)

    try:
        res = list(imap)
        pool.close()
        pool.join()
        results = []
        for result in res:
            if result is not None:
                results.append(result)
        return results

    except KeyboardInterrupt:
        pool.terminate()
        pool.join()
        raise
