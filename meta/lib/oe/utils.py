#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import subprocess
import traceback
import errno
import os

import bb.parse
from bb import multiprocessing

def read_file(filename):
    """
    Read a file under the path ``filename`` and return the content of the
    file. In case of :py:class:`IOError`, return an empty string. In case
    of other exceptions, return ``None``.
    """
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
    """
    If ``condition`` is ``True``, return the value of ``iftrue``, else
    return the value of ``iffalse``.
    """
    if condition:
        return iftrue
    else:
        return iffalse

def conditional(variable, checkvalue, truevalue, falsevalue, d):
    """
    If the value of ``variable`` (obtained using ``d``, the :term:`datastore`)
    equals the value of ``checkvalue``, return the value of ``truevalue``, else
    return the value of ``falsevalue``.
    """
    if d.getVar(variable) == checkvalue:
        return truevalue
    else:
        return falsevalue

def vartrue(var, iftrue, iffalse, d):
    """
    Check if the value of ``var`` (obtained using ``d``, the :term:`datastore`)
    passed to :py:func:`oe.types.boolean` returns ``True`` and return the value
    of ``iftrue`` if it does, otherwise return the value of ``iffalse``.
    """
    import oe.types
    if oe.types.boolean(d.getVar(var)):
        return iftrue
    else:
        return iffalse

def less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    """
    Check if the value of ``variable`` (obtained using ``d``, the
    :term:`datastore`) is inferior or equal to ``checkvalue``, and return the
    value of ``truevalue`` if it is, otherwise return the value of
    ``falsevalue``. Both ``checkvalue`` and the value of ``variable`` are
    converted to :py:type:`float` numbers before comparison.
    """
    if float(d.getVar(variable)) <= float(checkvalue):
        return truevalue
    else:
        return falsevalue

def version_less_or_equal(variable, checkvalue, truevalue, falsevalue, d):
    """
    Check if the value of ``variable`` (obtained using ``d``, the
    :term:`datastore`) is inferior or equal to ``checkvalue`` using
    :py:func:`oe.utils.vercmp_string`, and return the value of
    ``truevalue`` if it is, otherwise return the value of ``falsevalue``.
    """
    result = bb.utils.vercmp_string(d.getVar(variable), checkvalue)
    if result <= 0:
        return truevalue
    else:
        return falsevalue

def both_contain(variable1, variable2, checkvalue, d):
    """
    Check whether the value of the variables ``variable1`` and ``variable2``
    (obtained using ``d``, the :term:`datastore`) both contain all the values in
    ``checkvalue``. ``variable1``, ``variable2`` and ``checkvalue`` are
    treated as space-separated lists (``checkvalue`` can be a space-separated
    string, or directly a Python list).
    """
    val1 = d.getVar(variable1)
    val2 = d.getVar(variable2)
    val1 = set(val1.split())
    val2 = set(val2.split())
    if isinstance(checkvalue, str):
        checkvalue = set(checkvalue.split())
    else:
        checkvalue = set(checkvalue)
    if checkvalue.issubset(val1) and checkvalue.issubset(val2):
        return " ".join(checkvalue)
    else:
        return ""

def set_intersect(variable1, variable2, d):
    """
    Take the values of ``variable1`` and ``variable2`` (obtained using ``d``,
    the :term:`datastore`), convert them from space-separated lists to Python
    sets, and return the intersection as a space-separated list.

    For example::

       FOO = "a b c"
       BAR = "b c d"

    Then:

    .. code-block:: python

       >>> oe.utils.set_intersect("FOO", "BAR", d)
       "b c"
    """
    val1 = set(d.getVar(variable1).split())
    val2 = set(d.getVar(variable2).split())
    return " ".join(val1 & val2)

def set_difference(variable1, variable2, d):
    """
    Take the values of ``variable1`` and ``variable2`` (obtained using ``d``,
    the :term:`datastore`), convert them from space-separated lists to Python
    sets, and return a space-separated list of the elements in ``variable1``
    that are not in ``variable2``.

    For example::

       FOO = "a b c"
       BAR = "b c d"

    Then:

    .. code-block:: python

       >>> oe.utils.set_difference("FOO", "BAR", d)
       "a"

    If "*" is present in the value of ``variable2`` (not part of a larger 
    identifier), this is interpreted as "remove all entries":

    For example::

       FOO = "a b c"
       BAR = "*"

    Then:

    .. code-block:: python

       >>> oe.utils.set_difference("FOO", "BAR", d)
       ""
    """
    val1 = d.getVar(variable1)
    if not val1:
        return ""
    val2 = d.getVar(variable2)
    if not val2:
        return val1

    val1 = set(val1.split())
    val2 = set(val2.split())

    if "*" in val2:
        return ""

    # Return a sorted string to ensure that the result is consistent between
    # parser runs.
    return " ".join(sorted(val1 - val2))

def prune_suffix(var, suffixes, d):
    """
    See if the variable ``var`` ends with any of the suffixes listed and
    remove it if found.

    Also remove the :term:`MLPREFIX` prefix if present in ``var``.
    """
    for suffix in suffixes:
        if suffix and var.endswith(suffix):
            var = var[:-len(suffix)]

    prefix = d.getVar("MLPREFIX")
    if prefix and var.startswith(prefix):
        var = var[len(prefix):]

    return var

def build_depends_string(depends, task):
    """
    Append a taskname ``task`` to a space-separated list of dependencies stored in
    ``depends`` as used by the ``[depends]`` variable flag:

    .. code-block:: python

       >>> oe.utils.build_depends_string("dep1 dep2 dep3", "task")
       "dep1:task dep2:task dep3:task"
    """
    return " ".join(dep + ":" + task for dep in depends.split())

def inherits(d, *classes):
    """
    Return ``True`` if the metadata (obtained from ``d``, the
    :term:`datastore`) inherits *any* of the specified classes in
    ``*classes`` (internally uses :py:func:`bb.data.inherits_class`).
    """
    return any(bb.data.inherits_class(cls, d) for cls in classes)

def filter_default_features(varname, d):
    # Process default features to exclude features which the user has opted out
    # of. The result is appended to the target variable (e.g. DISTRO_FEATURES
    # or MACHINE_FEATURES).
    default_features = set_difference(varname + "_DEFAULTS",
                                      varname + "_OPTED_OUT",
                                      d)
    return default_features

def class_filter_features(defaults, features_var, filter_var, d):
    features = set(d.getVar(features_var).split())
    filtered = set(bb.utils.filter_string(defaults, d.getVar(filter_var)).split())
    return " ".join(sorted(features | filtered))

def set_class_filter(var, features_var, filter_var, d):
    defaults = d.getVar(var)
    if "}" in defaults:
        issues = [c for c in defaults.split() if "}" in c]
        for issue in issues:
            defaults = defaults.replace(issue, "")
            bb.warn("Unexpanded variable %s in %s is not recommended" % (issue, var))
    d.setVar(var, '${@oe.utils.class_filter_features("' + defaults + '", "' + features_var + '", "' + filter_var + '", d)}')

def parallel_make_value(pm):
    """
    Return the integer value for the number of parallel threads to use when
    building, scraped out of given string ``pm``. If no parallelization option is
    found, return an empty string.

    For example if ``pm`` is "-j 10", this will return 10 as an integer.

    This function is used by :py:func:`oe.utils.parallel_make`.
    """
    # look for '-j' and throw other options (e.g. '-l') away
    while pm:
        opt = pm.pop(0)
        if opt == '-j':
            v = pm.pop(0)
        elif opt.startswith('-j'):
            v = opt[2:].strip()
        else:
            continue

        return int(v)

    return ''

def parallel_make(d, makeinst=False):
    """
    Return the integer value for the number of parallel threads to use when
    building, scraped out of :term:`PARALLEL_MAKE`. If no parallelization option is
    found, return an empty string.

    For example if ``PARALLEL_MAKE = "-j 10"``, this will return 10 as an integer.

    The ``makeinst`` boolean can be set to ``True`` if the value should be
    obtained from :term:`PARALLEL_MAKEINST` instead.
    """
    if makeinst:
        pm = (d.getVar('PARALLEL_MAKEINST') or '').split()
    else:
        pm = (d.getVar('PARALLEL_MAKE') or '').split()
    return parallel_make_value(pm)

def parallel_make_argument(d, fmt, limit=None, makeinst=False):
    """
    Helper utility to construct a parallel make argument from the number of
    parallel threads specified in :term:`PARALLEL_MAKE`.

    Return the input format string ``fmt`` where a single ``%d`` will be expanded
    with the number of parallel threads to use. If ``limit`` is specified, the
    number of parallel threads will be no larger than it. If no parallelization
    option is found in :term:`PARALLEL_MAKE`, return an empty string.

    For example if ``PARALLEL_MAKE = "-j 10"`` then:

    .. code-block:: python

       >>> parallel_make_argument(d, "-n %d")
       "-n 10"
    """
    v = parallel_make(d, makeinst)
    if v:
        if limit:
            v = min(limit, v)
        return fmt % v
    return ''

def packages_filter_out_system(d):
    """
    Return a list of packages from :term:`PACKAGES` (obtained using ``d``, the
    :term:`datastore`) with the "system" packages such as ``${PN}-dbg``, ``${PN}-doc``,
    or ``${PN}-locale-en-gb`` removed.
    """
    pn = d.getVar('PN')
    pkgfilter = [pn + suffix for suffix in ('', '-dbg', '-dev', '-doc', '-locale', '-staticdev', '-src', '-lic')]
    localepkg = pn + "-locale-"
    pkgs = []

    for pkg in d.getVar('PACKAGES').split():
        if pkg not in pkgfilter and localepkg not in pkg:
            pkgs.append(pkg)
    return pkgs

def getstatusoutput(cmd):
    return subprocess.getstatusoutput(cmd)


def trim_version(version, num_parts=2):
    """
    Return just the first ``num_parts`` parts of ``version``, split by periods
    (``.``).  For example:

    .. code-block:: python

       >>> oe.utils.trim_version("1.2.3", 2)
       "1.2"
    """
    if type(version) is not str:
        raise TypeError("Version should be a string")
    if num_parts < 1:
        raise ValueError("Cannot split to parts < 1")

    parts = version.split(".")
    trimmed = ".".join(parts[:num_parts])
    return trimmed

def cpu_count(at_least=1, at_most=64):
    cpus = len(os.sched_getaffinity(0))
    return max(min(cpus, at_most), at_least)

def execute_pre_post_process(d, cmds):
    if cmds is None:
        return

    cmds = cmds.replace(";", " ")

    for cmd in cmds.split():
        bb.note("Executing %s ..." % cmd)
        bb.build.exec_func(cmd, d)

@bb.parse.vardepsexclude("BB_NUMBER_THREADS")
def get_bb_number_threads(d):
    """
    Return the value of :term:`BB_NUMBER_THREADS` (using ``d``, the
    :term:`datastore`). If undefined, return the value of
    :py:func:`os.cpu_count` or 1 if the former returns ``None``.
    """
    return int(d.getVar("BB_NUMBER_THREADS") or os.cpu_count() or 1)

def multiprocess_launch(target, items, d, extraargs=None):
    max_process = get_bb_number_threads(d)
    return multiprocess_launch_mp(target, items, max_process, extraargs)

# For each item in items, call the function 'target' with item as the first
# argument, extraargs as the other arguments and handle any exceptions in the
# parent thread
def multiprocess_launch_mp(target, items, max_process, extraargs=None):

    class ProcessLaunch(multiprocessing.Process):
        def __init__(self, *args, **kwargs):
            multiprocessing.Process.__init__(self, *args, **kwargs)
            self._pconn, self._cconn = multiprocessing.Pipe()
            self._exception = None
            self._result = None

        def run(self):
            try:
                ret = self._target(*self._args, **self._kwargs)
                self._cconn.send((None, ret))
            except Exception as e:
                tb = traceback.format_exc()
                self._cconn.send((e, tb))

        def update(self):
            if self._pconn.poll():
                (e, tb) = self._pconn.recv()
                if e is not None:
                    self._exception = (e, tb)
                else:
                    self._result = tb

        @property
        def exception(self):
            self.update()
            return self._exception

        @property
        def result(self):
            self.update()
            return self._result

    launched = []
    errors = []
    results = []
    items = list(items)
    while (items and not errors) or launched:
        if not errors and items and len(launched) < max_process:
            args = items.pop()
            if not type(args) is tuple:
                args = (args,)
            if extraargs is not None:
                args = args + extraargs
            p = ProcessLaunch(target=target, args=args)
            p.start()
            launched.append(p)
        for q in launched:
            # Have to manually call update() to avoid deadlocks. The pipe can be full and
            # transfer stalled until we try and read the results object but the subprocess won't exit
            # as it still has data to write (https://bugs.python.org/issue8426)
            q.update()
            # The finished processes are joined when calling is_alive()
            if not q.is_alive():
                if q.exception:
                    errors.append(q.exception)
                if q.result:
                    results.append(q.result)
                launched.remove(q)
    # Paranoia doesn't hurt
    for p in launched:
        p.join()
    if errors:
        msg = ""
        for (e, tb) in errors:
            if isinstance(e, subprocess.CalledProcessError) and e.output:
                msg = msg + str(e) + "\n"
                msg = msg + "Subprocess output:"
                msg = msg + e.output.decode("utf-8", errors="ignore")
            else:
                msg = msg + str(e) + ": " + str(tb) + "\n"
        bb.fatal("Fatal errors occurred in subprocesses:\n%s" % msg)
    return results

def squashspaces(string):
    """
    Return the input ``string`` with every occurrence of two or more whitespace
    replaced by only one whitespace.
    """
    import re
    return re.sub(r"\s+", " ", string).strip()

def rprovides_map(pkgdata_dir, pkg_dict):
    # Map file -> pkg provider
    rprov_map = {}

    for pkg in pkg_dict:
        path_to_pkgfile = os.path.join(pkgdata_dir, 'runtime-reverse', pkg)
        if not os.path.isfile(path_to_pkgfile):
            continue
        with open(path_to_pkgfile) as f:
            for line in f:
                if line.startswith('RPROVIDES') or line.startswith('FILERPROVIDES'):
                    # List all components provided by pkg.
                    # Exclude version strings, i.e. those starting with (
                    provides = [x for x in line.split()[1:] if not x.startswith('(')]
                    for prov in provides:
                        if prov in rprov_map:
                            rprov_map[prov].append(pkg)
                        else:
                            rprov_map[prov] = [pkg]

    return rprov_map

def format_pkg_list(pkg_dict, ret_format=None, pkgdata_dir=None):
    output = []

    if ret_format == "arch":
        for pkg in sorted(pkg_dict):
            output.append("%s %s" % (pkg, pkg_dict[pkg]["arch"]))
    elif ret_format == "file":
        for pkg in sorted(pkg_dict):
            output.append("%s %s %s" % (pkg, pkg_dict[pkg]["filename"], pkg_dict[pkg]["arch"]))
    elif ret_format == "ver":
        for pkg in sorted(pkg_dict):
            output.append("%s %s %s" % (pkg, pkg_dict[pkg]["arch"], pkg_dict[pkg]["ver"]))
    elif ret_format == "deps":
        rprov_map = rprovides_map(pkgdata_dir, pkg_dict)
        for pkg in sorted(pkg_dict):
            for dep in pkg_dict[pkg]["deps"]:
                if dep in rprov_map:
                    # There could be multiple providers within the image
                    for pkg_provider in rprov_map[dep]:
                        output.append("%s|%s * %s [RPROVIDES]" % (pkg, pkg_provider, dep))
                else:
                    output.append("%s|%s" % (pkg, dep))
    else:
        for pkg in sorted(pkg_dict):
            output.append(pkg)

    output_str = '\n'.join(output)

    if output_str:
        # make sure last line is newline terminated
        output_str += '\n'

    return output_str


# Helper function to get the host gcc version
def get_host_gcc_version(d, taskcontextonly=False):
    import re, subprocess

    if taskcontextonly and d.getVar('BB_WORKERCONTEXT') != '1':
        return

    try:
        env = os.environ.copy()
        # datastore PATH does not contain session PATH as set by environment-setup-...
        # this breaks the install-buildtools use-case
        # env["PATH"] = d.getVar("PATH")
        output = subprocess.check_output(["gcc", "--version"], \
                    env=env, stderr=subprocess.STDOUT).decode("utf-8")
    except subprocess.CalledProcessError as e:
        bb.fatal("Error running gcc --version: %s" % (e.output.decode("utf-8")))
    except OSError as e:
        bb.fatal("Error running gcc --version: %s" % e)

    match = re.match(r".* (\d+\.\d+)\.\d+.*", output.split('\n')[0])
    if not match:
        bb.fatal("Can't get compiler version from gcc --version output")

    version = match.group(1)
    return version

@bb.parse.vardepsexclude("DEFAULTTUNE_MULTILIB_ORIGINAL", "OVERRIDES")
def get_multilib_datastore(variant, d):
    localdata = bb.data.createCopy(d)
    if variant:
        overrides = localdata.getVar("OVERRIDES", False) + ":virtclass-multilib-" + variant
        localdata.setVar("OVERRIDES", overrides)
        localdata.setVar("MLPREFIX", variant + "-")
    else:
        origdefault = localdata.getVar("DEFAULTTUNE_MULTILIB_ORIGINAL")
        if origdefault:
            localdata.setVar("DEFAULTTUNE", origdefault)
        overrides = localdata.getVar("OVERRIDES", False).split(":")
        overrides = ":".join([x for x in overrides if not x.startswith("virtclass-multilib-")])
        localdata.setVar("OVERRIDES", overrides)
        localdata.setVar("MLPREFIX", "")
    return localdata

def sh_quote(string):
    """
    Return a shell-escaped version of ``string`` using :py:func:`shlex.quote`.
    """
    import shlex
    return shlex.quote(string)

def directory_size(root, blocksize=4096):
    """
    Calculate and return the size of the directory ``root``, taking into account
    hard links, rounding up every size to multiples of ``blocksize``.
    """
    def roundup(size):
        """
        Round the size up to the nearest multiple of the block size.
        """
        import math
        return math.ceil(size / blocksize) * blocksize

    def getsize(filename):
        """
        Get the size of the filename, not following symlinks, taking into
        account hard links.
        """
        stat = os.lstat(filename)
        if stat.st_ino not in inodes:
            inodes.add(stat.st_ino)
            return stat.st_size
        else:
            return 0

    inodes = set()
    total = 0
    for root, dirs, files in os.walk(root):
        total += sum(roundup(getsize(os.path.join(root, name))) for name in files)
        total += roundup(getsize(root))
    return total


def touch(filename):
    """
    Update the ``mtime`` of a file under the path ``filename``,
    skip if permission/read-only issues.

    No return value.
    """
    try:
        os.utime(filename, None)
    except PermissionError:
        pass
    except OSError as e:
        # Handle read-only file systems gracefully
        if e.errno != errno.EROFS:
            raise e

#
# Set datastore variables to convert to an architecture independent state
# Used by allarch recipes and other cases where arch independence is needed
#
def make_arch_independent(d):
    # Set these to a common set of values, we shouldn't be using them other that for WORKDIR directory
    # naming anyway
    d.setVar("baselib", "lib")
    d.setVar("TARGET_ARCH", "allarch")
    d.setVar("TARGET_OS", "linux")
    d.setVar("TARGET_CC_ARCH", "none")
    d.setVar("TARGET_LD_ARCH", "none")
    d.setVar("TARGET_AS_ARCH", "none")
    d.setVar("TARGET_FPU", "")
    d.setVar("TARGET_PREFIX", "")
    # Expand PACKAGE_EXTRA_ARCHS since the staging code needs this
    # (this removes any dependencies from the hash perspective)
    d.setVar("PACKAGE_EXTRA_ARCHS", d.getVar("PACKAGE_EXTRA_ARCHS"))
    d.setVar("SDK_ARCH", "none")
    d.setVar("SDK_CC_ARCH", "none")
    d.setVar("TARGET_CPPFLAGS", "none")
    d.setVar("TARGET_CFLAGS", "none")
    d.setVar("TARGET_CXXFLAGS", "none")
    d.setVar("TARGET_LDFLAGS", "none")
    d.setVar("POPULATESYSROOTDEPS", "")

    # Avoid this being unnecessarily different due to nuances of
    # the target machine that aren't important for "all" arch
    # packages.
    d.setVar("LDFLAGS", "")

    # No need to do shared library processing or debug symbol handling
    d.setVar("EXCLUDE_FROM_SHLIBS", "1")
    d.setVar("INHIBIT_PACKAGE_DEBUG_SPLIT", "1")
    d.setVar("INHIBIT_PACKAGE_STRIP", "1")

    # These multilib values shouldn't change allarch packages so exclude them
    d.appendVarFlag("emit_pkgdata", "vardepsexclude", " MULTILIB_VARIANTS")
    d.appendVarFlag("write_specfile", "vardepsexclude", " MULTILIBS")
    d.appendVarFlag("do_package", "vardepsexclude", " package_do_shlibs")

    d.setVar("qemu_wrapper_cmdline", "def qemu_wrapper_cmdline(data, rootfs_path, library_paths):\n    return 'false'")

def base_set_filespath(path, d):
    filespath = []
    extrapaths = (d.getVar("FILESEXTRAPATHS") or "")
    # Remove default flag which was used for checking
    extrapaths = extrapaths.replace("__default:", "")
    # Don't prepend empty strings to the path list
    if extrapaths != "":
        path = extrapaths.split(":") + path
    # The ":" ensures we have an 'empty' override
    overrides = (":" + (d.getVar("FILESOVERRIDES") or "")).split(":")
    overrides.reverse()
    for o in overrides:
        for p in path:
            if p != "":
                filespath.append(os.path.join(p, o))
    return ":".join(filespath)

def check_app_exists(app, d):
    """
    Using :py:func:`bb.utils.which`, check if the program used by the command
    stored in ``app`` exists.
    """
    app = d.expand(app).split()[0].strip()
    path = d.getVar('PATH')
    return bool(bb.utils.which(path, app))

def all_multilib_tune_values(d, var, unique = True, need_split = True, delim = ' '):
    """
    Return a space-separated list of ``var`` in all multilib tune configurations.
    ``unique`` removes repetitions in the string. ``need_split`` should be set
    if the input ``var`` is a ``delim``-separated list that should be split
    (otherwise an iterable object can be passed with ``need_split=False``).
    """
    values = []
    variants = (d.getVar("MULTILIB_VARIANTS") or "").split() + ['']
    for item in variants:
        localdata = get_multilib_datastore(item, d)
        # We need WORKDIR to be consistent with the original datastore
        localdata.setVar("WORKDIR", d.getVar("WORKDIR"))
        value = localdata.getVar(var) or ""
        if value != "":
            if need_split:
                for item in value.split(delim):
                    values.append(item)
            else:
                values.append(value)
    if unique:
        #we do this to keep order as much as possible
        ret = []
        for value in values:
            if not value in ret:
                ret.append(value)
    else:
        ret = values
    return " ".join(ret)

def extend_variants(d, var, extend, delim=':'):
    """
    Return a space-separated string of all BitBake class extend variants
    for the given ``extend``, extracted from the ``delim``-separated entries of
    the value of ``var`` (obtained using ``d``, the :term:`datastore`).

    For example, consider the following assignment::

       MULTILIBS = "multilib:lib32 multilib:lib64"

    Then:

    .. code-block:: python

       >>> oe.utils.extend_variants(d, "MULTILIBS", "multilib")
       "lib32 lib64"
    """
    variants = []
    whole = d.getVar(var) or ""
    for ext in whole.split():
        eext = ext.split(delim)
        if len(eext) > 1 and eext[0] == extend:
            variants.append(eext[1])
    return " ".join(variants)

def multilib_pkg_extend(d, pkg):
    variants = (d.getVar("MULTILIB_VARIANTS") or "").split()
    if not variants:
        return pkg
    pkgs = pkg
    for v in variants:
        pkgs = pkgs + " " + v + "-" + pkg
    return pkgs

