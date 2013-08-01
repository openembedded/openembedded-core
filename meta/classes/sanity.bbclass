#
# Sanity check the users setup for common misconfigurations
#

SANITY_REQUIRED_UTILITIES ?= "patch diffstat makeinfo git bzip2 tar gzip gawk chrpath wget cpio"

def bblayers_conf_file(d):
    return os.path.join(d.getVar('TOPDIR', True), 'conf/bblayers.conf')

def sanity_conf_read(fn):
    with open(fn, 'r') as f:
        lines = f.readlines()
    return lines

def sanity_conf_find_line(pattern, lines):
    import re
    return next(((index, line)
        for index, line in enumerate(lines)
        if re.search(pattern, line)), (None, None))

def sanity_conf_update(fn, lines, version_var_name, new_version):
    index, line = sanity_conf_find_line(version_var_name, lines)
    lines[index] = '%s = "%d"\n' % (version_var_name, new_version)
    with open(fn, "w") as f:
        f.write(''.join(lines))

EXPORT_FUNCTIONS bblayers_conf_file sanity_conf_read sanity_conf_find_line sanity_conf_update

# Functions added to this variable MUST throw an exception (or sys.exit()) unless they
# successfully changed LCONF_VERSION in bblayers.conf
BBLAYERS_CONF_UPDATE_FUNCS += "oecore_update_bblayers"

python oecore_update_bblayers() {
    # bblayers.conf is out of date, so see if we can resolve that

    current_lconf = int(d.getVar('LCONF_VERSION', True))
    if not current_lconf:
        sys.exit()
    lconf_version = int(d.getVar('LAYER_CONF_VERSION', True))
    lines = []

    if current_lconf < 4:
        sys.exit()

    bblayers_fn = bblayers_conf_file(d)
    lines = sanity_conf_read(bblayers_fn)

    if current_lconf == 4 and lconf_version > 4:
        topdir_var = '$' + '{TOPDIR}'
        index, bbpath_line = sanity_conf_find_line('BBPATH', lines)
        if bbpath_line:
            start = bbpath_line.find('"')
            if start != -1 and (len(bbpath_line) != (start + 1)):
                if bbpath_line[start + 1] == '"':
                    lines[index] = (bbpath_line[:start + 1] +
                                    topdir_var + bbpath_line[start + 1:])
                else:
                    if not topdir_var in bbpath_line:
                        lines[index] = (bbpath_line[:start + 1] +
                                    topdir_var + ':' + bbpath_line[start + 1:])
            else:
                sys.exit()
        else:
            index, bbfiles_line = sanity_conf_find_line('BBFILES', lines)
            if bbfiles_line:
                lines.insert(index, 'BBPATH = "' + topdir_var + '"\n')
            else:
                sys.exit()

        current_lconf += 1
        sanity_conf_update(bblayers_fn, lines, 'LCONF_VERSION', current_lconf)
        return

    sys.exit()
}

def raise_sanity_error(msg, d, network_error=False):
    if d.getVar("SANITY_USE_EVENTS", True) == "1":
        try:
            bb.event.fire(bb.event.SanityCheckFailed(msg, network_error), d)
        except TypeError:
            bb.event.fire(bb.event.SanityCheckFailed(msg), d)
        return

    bb.fatal(""" OE-core's config sanity checker detected a potential misconfiguration.
    Either fix the cause of this error or at your own risk disable the checker (see sanity.conf).
    Following is the list of potential problems / advisories:
    
    %s""" % msg)

# Check a single tune for validity.
def check_toolchain_tune(data, tune, multilib):
    tune_errors = []
    if not tune:
        return "No tuning found for %s multilib." % multilib
    bb.debug(2, "Sanity-checking tuning '%s' (%s) features:" % (tune, multilib))
    features = (data.getVar("TUNE_FEATURES_tune-%s" % tune, True) or "").split()
    if not features:
        return "Tuning '%s' has no defined features, and cannot be used." % tune
    valid_tunes = data.getVarFlags('TUNEVALID') or {}
    conflicts = data.getVarFlags('TUNECONFLICTS') or {}
    # [doc] is the documentation for the variable, not a real feature
    if 'doc' in valid_tunes:
        del valid_tunes['doc']
    if 'doc' in conflicts:
        del conflicts['doc']
    for feature in features:
        if feature in conflicts:
            for conflict in conflicts[feature].split():
                if conflict in features:
                    tune_errors.append("Feature '%s' conflicts with '%s'." %
                        (feature, conflict))
        if feature in valid_tunes:
            bb.debug(2, "  %s: %s" % (feature, valid_tunes[feature]))
        else:
            tune_errors.append("Feature '%s' is not defined." % feature)
    whitelist = data.getVar("TUNEABI_WHITELIST", True) or ''
    override = data.getVar("TUNEABI_OVERRIDE", True) or ''
    if whitelist:
        tuneabi = data.getVar("TUNEABI_tune-%s" % tune, True) or ''
        if not tuneabi:
            tuneabi = tune
        if True not in [x in whitelist.split() for x in tuneabi.split()]:
            tune_errors.append("Tuning '%s' (%s) cannot be used with any supported tuning/ABI." %
                (tune, tuneabi))
    if tune_errors:
        return "Tuning '%s' has the following errors:\n" % tune + '\n'.join(tune_errors)

def check_toolchain(data):
    tune_error_set = []
    deftune = data.getVar("DEFAULTTUNE", True)
    tune_errors = check_toolchain_tune(data, deftune, 'default')
    if tune_errors:
        tune_error_set.append(tune_errors)

    multilibs = (data.getVar("MULTILIB_VARIANTS", True) or "").split()
    global_multilibs = (data.getVar("MULTILIB_GLOBAL_VARIANTS", True) or "").split()

    if multilibs:
        seen_libs = []
        seen_tunes = []
        for lib in multilibs:
            if lib in seen_libs:
                tune_error_set.append("The multilib '%s' appears more than once." % lib)
            else:
                seen_libs.append(lib)
            if not lib in global_multilibs:
                tune_error_set.append("Multilib %s is not present in MULTILIB_GLOBAL_VARIANTS" % lib)
            tune = data.getVar("DEFAULTTUNE_virtclass-multilib-%s" % lib, True)
            if tune in seen_tunes:
                tune_error_set.append("The tuning '%s' appears in more than one multilib." % tune)
            else:
                seen_libs.append(tune)
            if tune == deftune:
                tune_error_set.append("Multilib '%s' (%s) is also the default tuning." % (lib, deftune))
            else:
                tune_errors = check_toolchain_tune(data, tune, lib)
            if tune_errors:
                tune_error_set.append(tune_errors)
    if tune_error_set:
        return "Toolchain tunings invalid:\n" + '\n'.join(tune_error_set)

    return ""

def check_conf_exists(fn, data):
    bbpath = []
    fn = data.expand(fn)
    vbbpath = data.getVar("BBPATH")
    if vbbpath:
        bbpath += vbbpath.split(":")
    for p in bbpath:
        currname = os.path.join(data.expand(p), fn)
        if os.access(currname, os.R_OK):
            return True
    return False

def check_sanity_sstate_dir_change(sstate_dir, data):
    # Sanity checks to be done when the value of SSTATE_DIR changes

    # Check that SSTATE_DIR isn't on a filesystem with limited filename length (eg. eCryptFS)
    testmsg = ""
    if sstate_dir != "":
        testmsg = check_create_long_filename(sstate_dir, "SSTATE_DIR")
        # If we don't have permissions to SSTATE_DIR, suggest the user set it as an SSTATE_MIRRORS
        try:
            err = testmsg.split(': ')[1].strip()
            if err == "Permission denied.":
                testmsg = testmsg + "You could try using %s in SSTATE_MIRRORS rather than as an SSTATE_CACHE.\n" % (sstate_dir)
        except IndexError:
            pass
    return testmsg

def check_sanity_tmpdir_change(tmpdir, data):
    # Sanity checks to be done when the value of TMPDIR changes

    # Check that TMPDIR isn't on a filesystem with limited filename length (eg. eCryptFS)
    testmsg = check_create_long_filename(tmpdir, "TMPDIR")

    # Some third-party software apparently relies on chmod etc. being suid root (!!)
    import stat
    suid_check_bins = "chown chmod mknod".split()
    for bin_cmd in suid_check_bins:
        bin_path = bb.utils.which(os.environ["PATH"], bin_cmd)
        if bin_path:
            bin_stat = os.stat(bin_path)
            if bin_stat.st_uid == 0 and bin_stat.st_mode & stat.S_ISUID:
                testmsg = testmsg + '%s has the setuid bit set. This interferes with pseudo and may cause other issues that break the build process.\n' % bin_path

    # Check that we can fetch from various network transports
    errmsg = check_connectivity(data)
    testmsg = testmsg + check_connectivity(data)
    return testmsg, errmsg != ""
        
def check_sanity_version_change(data):
    # Sanity checks to be done when SANITY_VERSION changes
    return ""

def check_pseudo_wrapper():
    import sys
    if not sys.argv[0].endswith('/bitbake'):
        return ""

    import subprocess as sub
    # Check if bitbake wrapper is being used
    pseudo_build = os.environ.get( 'PSEUDO_BUILD' )
    if not pseudo_build:
        bb.warn("Bitbake has not been run using the bitbake wrapper (scripts/bitbake); this is likely because your PATH has been altered from that normally set up by the oe-init-build-env script. Not using the wrapper may result in failures during package installation, so it is highly recommended that you set your PATH back so that the wrapper script is being executed.")

    if (not pseudo_build) or pseudo_build == '2':
        # pseudo ought to be working, let's see if it is...
        p = sub.Popen(['sh', '-c', 'PSEUDO_DISABLED=0 id -u'],stdout=sub.PIPE,stderr=sub.PIPE)
        out, err = p.communicate()
        if out.rstrip() != '0':
            msg = "Pseudo is not functioning correctly, which will cause failures during package installation. Please check your configuration."
            if pseudo_build == '2':
                return msg
            else:
                bb.warn(msg)
    return ""

def check_create_long_filename(filepath, pathname):
    testfile = os.path.join(filepath, ''.join([`num`[-1] for num in xrange(1,200)]))
    try:
        if not os.path.exists(filepath):
            bb.utils.mkdirhier(filepath)
        f = file(testfile, "w")
        f.close()
        os.remove(testfile)
    except IOError as (errno, strerror):
        if errno == 36: # ENAMETOOLONG
            return "Failed to create a file with a long name in %s. Please use a filesystem that does not unreasonably limit filename length.\n" % pathname
        else:
            return "Failed to create a file in %s: %s.\n" % (pathname, strerror)
    except OSError as (errno, strerror):
        return "Failed to create %s directory in which to run long name sanity check: %s.\n" % (pathname, strerror)
    return ""

def check_path_length(filepath, pathname, limit):
    if len(filepath) > limit:
        return "The length of %s is longer than 410, this would cause unexpected errors, please use a shorter path.\n" % pathname
    return ""

def check_connectivity(d):
    # URI's to check can be set in the CONNECTIVITY_CHECK_URIS variable
    # using the same syntax as for SRC_URI. If the variable is not set
    # the check is skipped
    test_uris = (d.getVar('CONNECTIVITY_CHECK_URIS', True) or "").split()
    retval = ""

    # Only check connectivity if network enabled and the
    # CONNECTIVITY_CHECK_URIS are set
    network_enabled = not d.getVar('BB_NO_NETWORK', True)
    check_enabled = len(test_uris)
    # Take a copy of the data store and unset MIRRORS and PREMIRROS
    data = bb.data.createCopy(d)
    data.delVar('PREMIRRORS')
    data.delVar('MIRRORS')
    if check_enabled and network_enabled:
        try:
            fetcher = bb.fetch2.Fetch(test_uris, data)
            fetcher.checkstatus()
        except Exception:
            # Allow the message to be configured so that users can be
            # pointed to a support mechanism.
            msg = data.getVar('CONNECTIVITY_CHECK_MSG', True) or ""
            if len(msg) == 0:
                msg = "Failed to fetch test data from the network. Please ensure your network is configured correctly.\n"
            retval = msg

    return retval

def check_supported_distro(sanity_data):
    tested_distros = sanity_data.getVar('SANITY_TESTED_DISTROS', True)
    if not tested_distros:
        return

    try:
        distro = oe.lsb.distro_identifier()
    except Exception:
        distro = None

    if distro:
        if distro not in [x.strip() for x in tested_distros.split('\\n')]:
            bb.warn('Host distribution "%s" has not been validated with this version of the build system; you may possibly experience unexpected failures. It is recommended that you use a tested distribution.' % distro)
    else:
        bb.warn('Host distribution could not be determined; you may possibly experience unexpected failures. It is recommended that you use a tested distribution.')

# Checks we should only make if MACHINE is set correctly
def check_sanity_validmachine(sanity_data):
    messages = ""

    # Check TUNE_ARCH is set
    if sanity_data.getVar('TUNE_ARCH', True) == 'INVALID':
        messages = messages + 'TUNE_ARCH is unset. Please ensure your MACHINE configuration includes a valid tune configuration file which will set this correctly.\n'

    # Check TARGET_OS is set
    if sanity_data.getVar('TARGET_OS', True) == 'INVALID':
        messages = messages + 'Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.\n'

    # Check that we don't have duplicate entries in PACKAGE_ARCHS & that TUNE_PKGARCH is in PACKAGE_ARCHS
    pkgarchs = sanity_data.getVar('PACKAGE_ARCHS', True)
    tunepkg = sanity_data.getVar('TUNE_PKGARCH', True)
    tunefound = False
    seen = {}
    dups = []

    for pa in pkgarchs.split():
        if seen.get(pa, 0) == 1:
            dups.append(pa)
        else:
            seen[pa] = 1
        if pa == tunepkg:
            tunefound = True

    if len(dups):
        messages = messages + "Error, the PACKAGE_ARCHS variable contains duplicates. The following archs are listed more than once: %s" % " ".join(dups)

    if tunefound == False:
        messages = messages + "Error, the PACKAGE_ARCHS variable does not contain TUNE_PKGARCH (%s)." % tunepkg

    return messages

# Checks if necessary to add option march to host gcc
def check_gcc_march(sanity_data):
    result = False

    # Check if -march not in BUILD_CFLAGS
    if sanity_data.getVar("BUILD_CFLAGS",True).find("-march") < 0:

        # Construct a test file
        f = file("gcc_test.c", "w")
        f.write("int main (){ __GCC_HAVE_SYNC_COMPARE_AND_SWAP_4; return 0;}\n")
        f.close()
        import commands

        # Check if GCC could work without march
        status,result = commands.getstatusoutput("${BUILD_PREFIX}gcc gcc_test.c -o gcc_test")
        if status != 0:
            # Check if GCC could work with march
            status,result = commands.getstatusoutput("${BUILD_PREFIX}gcc -march=native gcc_test.c -o gcc_test")
            if status == 0: 
                result = True
            else:
                result = False

        os.remove("gcc_test.c")
        if os.path.exists("gcc_test"):
            os.remove("gcc_test")

    return result

def check_sanity(sanity_data):
    import subprocess

    reparse = False
    try:
        from distutils.version import LooseVersion
    except ImportError:
        def LooseVersion(v): print "WARNING: sanity.bbclass can't compare versions without python-distutils"; return 1
    import commands

    # Check the bitbake version meets minimum requirements
    minversion = sanity_data.getVar('BB_MIN_VERSION', True)
    if not minversion:
        # Hack: BB_MIN_VERSION hasn't been parsed yet so return 
        # and wait for the next call
        print "Foo %s" % minversion
        return

    if 0 == os.getuid():
        raise_sanity_error("Do not use Bitbake as root.", sanity_data)

    messages = ""

    # Check the Python version, we now use Python 2.6 features in
    # various classes
    import sys
    if sys.hexversion < 0x020600F0:
        messages = messages + 'The system requires at least Python 2.6 to run. Please update your Python interpreter.\n'

    if (LooseVersion(bb.__version__) < LooseVersion(minversion)):
        messages = messages + 'Bitbake version %s is required and version %s was found\n' % (minversion, bb.__version__)

    # Check that the MACHINE is valid, if it is set
    machinevalid = True
    if sanity_data.getVar('MACHINE', True):
        if not check_conf_exists("conf/machine/${MACHINE}.conf", sanity_data):
            messages = messages + 'Please set a valid MACHINE in your local.conf or environment\n'
            machinevalid = False
        else:
            messages = messages + check_sanity_validmachine(sanity_data)
    else:
        messages = messages + 'Please set a MACHINE in your local.conf or environment\n'
        machinevalid = False

    # Check we are using a valid local.conf
    current_conf  = sanity_data.getVar('CONF_VERSION', True)
    conf_version =  sanity_data.getVar('LOCALCONF_VERSION', True)

    if current_conf != conf_version:
        messages = messages + "Your version of local.conf was generated from an older/newer version of local.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/local.conf ${COREBASE}/meta*/conf/local.conf.sample\" is a good way to visualise the changes.\n"

    # Check bblayers.conf is valid
    current_lconf = sanity_data.getVar('LCONF_VERSION', True)
    lconf_version = sanity_data.getVar('LAYER_CONF_VERSION', True)
    if current_lconf != lconf_version:
        funcs = sanity_data.getVar('BBLAYERS_CONF_UPDATE_FUNCS', True).split()
        for func in funcs:
            success = True
            try:
                bb.build.exec_func(func, sanity_data)
            except Exception:
                success = False
            if success:
                bb.note("Your conf/bblayers.conf has been automatically updated.")
                reparse = True
                break
        if not reparse:
            messages = messages + "Your version of bblayers.conf was generated from an older/newer version of bblayers.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/bblayers.conf ${COREBASE}/meta*/conf/bblayers.conf.sample\" is a good way to visualise the changes.\n"

    # If we have a site.conf, check it's valid
    if check_conf_exists("conf/site.conf", sanity_data):
        current_sconf = sanity_data.getVar('SCONF_VERSION', True)
        sconf_version = sanity_data.getVar('SITE_CONF_VERSION', True)
        if current_sconf != sconf_version:
            messages = messages + "Your version of site.conf was generated from an older version of site.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/site.conf ${COREBASE}/meta*/conf/site.conf.sample\" is a good way to visualise the changes.\n"

    assume_provided = sanity_data.getVar('ASSUME_PROVIDED', True).split()
    # Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
    if "diffstat-native" not in assume_provided:
        messages = messages + 'Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf\n'

    # Check that DL_DIR is set, exists and is writable. In theory, we should never even hit the check if DL_DIR isn't 
    # set, since so much relies on it being set.
    dldir = sanity_data.getVar('DL_DIR', True)
    if not dldir:
        messages = messages + "DL_DIR is not set. Your environment is misconfigured, check that DL_DIR is set, and if the directory exists, that it is writable. \n"
    if os.path.exists(dldir) and not os.access(dldir, os.W_OK):
        messages = messages + "DL_DIR: %s exists but you do not appear to have write access to it. \n" % dldir
    
    # Check that the DISTRO is valid, if set
    # need to take into account DISTRO renaming DISTRO
    distro = sanity_data.getVar('DISTRO', True)
    if distro:
        if not ( check_conf_exists("conf/distro/${DISTRO}.conf", sanity_data) or check_conf_exists("conf/distro/include/${DISTRO}.inc", sanity_data) ):
            messages = messages + "DISTRO '%s' not found. Please set a valid DISTRO in your local.conf\n" % sanity_data.getVar("DISTRO", True )

    missing = ""

    if not check_app_exists("${MAKE}", sanity_data):
        missing = missing + "GNU make,"

    if not check_app_exists('${BUILD_PREFIX}gcc', sanity_data):
        missing = missing + "C Compiler (%sgcc)," % sanity_data.getVar("BUILD_PREFIX", True)

    if not check_app_exists('${BUILD_PREFIX}g++', sanity_data):
        missing = missing + "C++ Compiler (%sg++)," % sanity_data.getVar("BUILD_PREFIX", True)

    required_utilities = sanity_data.getVar('SANITY_REQUIRED_UTILITIES', True)

    if "qemu-native" in assume_provided:
        if not check_app_exists("qemu-arm", sanity_data):
            messages = messages + "qemu-native was in ASSUME_PROVIDED but the QEMU binaries (qemu-arm) can't be found in PATH"

    if check_gcc_march(sanity_data):
        messages = messages + "Your gcc version is older than 4.5, please add the following param to local.conf\n \
        BUILD_CFLAGS_append = \" -march=native\"\n"

    paths = sanity_data.getVar('PATH', True).split(":")
    if "." in paths or "" in paths:
        messages = messages + "PATH contains '.' or '' (empty element), which will break the build, please remove this.\n"
        messages = messages + "Parsed PATH is " + str(paths) + "\n"

    bbpaths = sanity_data.getVar('BBPATH', True).split(":")
    if ("." in bbpaths or "" in bbpaths) and not reparse:
        # TODO: change the following message to fatal when all BBPATH issues
        # are fixed
        bb.warn("BBPATH references the current directory, either through "    \
                "an empty entry, or a '.'.\n\t This is unsafe and means your "\
                "layer configuration is adding empty elements to BBPATH.\n\t "\
                "Please check your layer.conf files and other BBPATH "        \
                "settings to remove the current working directory "           \
                "references.");
        bb.warn("Parsed BBPATH is" + str(bbpaths));

    if sanity_data.getVar('TARGET_ARCH', True) == "arm":
        # This path is no longer user-readable in modern (very recent) Linux
        try:
            if os.path.exists("/proc/sys/vm/mmap_min_addr"):
                f = open("/proc/sys/vm/mmap_min_addr", "r")
                try:
                    if (int(f.read().strip()) > 65536):
                        messages = messages + "/proc/sys/vm/mmap_min_addr is not <= 65536. This will cause problems with qemu so please fix the value (as root).\n\nTo fix this in later reboots, set vm.mmap_min_addr = 65536 in /etc/sysctl.conf.\n"
                finally:
                    f.close()
        except:
            pass

    for util in required_utilities.split():
        if not check_app_exists( util, sanity_data ):
            missing = missing + "%s," % util

    if missing != "":
        missing = missing.rstrip(',')
        messages = messages + "Please install the following missing utilities: %s\n" % missing

    pseudo_msg = check_pseudo_wrapper()
    if pseudo_msg != "":
        messages = messages + pseudo_msg + '\n'

    check_supported_distro(sanity_data)
    if machinevalid:
        toolchain_msg = check_toolchain(sanity_data)
        if toolchain_msg != "":
            messages = messages + toolchain_msg + '\n'

    # Check if DISPLAY is set if IMAGETEST is set
    if sanity_data.getVar( 'IMAGETEST', True ) == 'qemu':
        display = sanity_data.getVar("BB_ORIGENV", False).getVar("DISPLAY", True)
        if not display:
            messages = messages + 'qemuimagetest needs a X desktop to start qemu, please set DISPLAY correctly (e.g. DISPLAY=:1.0)\n'

    omask = os.umask(022)
    if omask & 0755:
        messages = messages + "Please use a umask which allows a+rx and u+rwx\n"
    os.umask(omask)

    oes_bb_conf = sanity_data.getVar( 'OES_BITBAKE_CONF', True)
    if not oes_bb_conf:
        messages = messages + 'You do not include the OpenEmbedded version of conf/bitbake.conf. This means your environment is misconfigured, in particular check BBPATH.\n'

    nolibs = sanity_data.getVar('NO32LIBS', True)
    if not nolibs:
        lib32path = '/lib'
        if os.path.exists('/lib64') and ( os.path.islink('/lib64') or os.path.islink('/lib') ):
           lib32path = '/lib32'

        if os.path.exists('%s/libc.so.6' % lib32path) and not os.path.exists('/usr/include/gnu/stubs-32.h'):
            messages = messages + "You have a 32-bit libc, but no 32-bit headers.  You must install the 32-bit libc headers.\n"

    tmpdir = sanity_data.getVar('TMPDIR', True)
    sstate_dir = sanity_data.getVar('SSTATE_DIR', True)

    # The length of tmpdir can't be longer than 410
    messages = messages + check_path_length(tmpdir, "TMPDIR", 410)

    # Check saved sanity info
    last_sanity_version = 0
    last_tmpdir = ""
    last_sstate_dir = ""
    sanityverfile = 'conf/sanity_info'
    if os.path.exists(sanityverfile):
        f = file(sanityverfile, 'r')
        for line in f:
            if line.startswith('SANITY_VERSION'):
                last_sanity_version = int(line.split()[1])
            if line.startswith('TMPDIR'):
                last_tmpdir = line.split()[1]
            if line.startswith('SSTATE_DIR'):
                last_sstate_dir = line.split()[1]
    
    sanity_version = int(sanity_data.getVar('SANITY_VERSION', True) or 1)
    network_error = False
    if last_sanity_version < sanity_version: 
        messages = messages + check_sanity_version_change(sanity_data)
        err, network_error = check_sanity_tmpdir_change(tmpdir, sanity_data)
        messages = messages + err
        messages = messages + check_sanity_sstate_dir_change(sstate_dir, sanity_data)
    else: 
        if last_tmpdir != tmpdir:
            err, network_error = check_sanity_tmpdir_change(tmpdir, sanity_data)
            messages = messages + err
        if last_sstate_dir != sstate_dir:
            messages = messages + check_sanity_sstate_dir_change(sstate_dir, sanity_data)
    if os.path.exists("conf") and not messages:
        f = file(sanityverfile, 'w')
        f.write("SANITY_VERSION %s\n" % sanity_version) 
        f.write("TMPDIR %s\n" % tmpdir) 
        f.write("SSTATE_DIR %s\n" % sstate_dir) 

    #
    # Check that TMPDIR hasn't changed location since the last time we were run
    #
    checkfile = os.path.join(tmpdir, "saved_tmpdir")
    if os.path.exists(checkfile):
        f = file(checkfile, "r")
        saved_tmpdir = f.read().strip()
        if (saved_tmpdir != tmpdir):
            messages = messages + "Error, TMPDIR has changed location. You need to either move it back to %s or rebuild\n" % saved_tmpdir
    else:
        bb.utils.mkdirhier(tmpdir)
        f = file(checkfile, "w")
        f.write(tmpdir)
    f.close()

    #
    # Check the 'ABI' of TMPDIR
    #
    current_abi = sanity_data.getVar('OELAYOUT_ABI', True)
    abifile = sanity_data.getVar('SANITY_ABIFILE', True)
    if os.path.exists(abifile):
        f = file(abifile, "r")
        abi = f.read().strip()
        if not abi.isdigit():
            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "2" and current_abi == "3":
            bb.note("Converting staging from layout version 2 to layout version 3")
            subprocess.call(sanity_data.expand("mv ${TMPDIR}/staging ${TMPDIR}/sysroots"), shell=True)
            subprocess.call(sanity_data.expand("ln -s sysroots ${TMPDIR}/staging"), shell=True)
            subprocess.call(sanity_data.expand("cd ${TMPDIR}/stamps; for i in */*do_populate_staging; do new=`echo $i | sed -e 's/do_populate_staging/do_populate_sysroot/'`; mv $i $new; done"), shell=True)
            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "3" and current_abi == "4":
            bb.note("Converting staging layout from version 3 to layout version 4")
            if os.path.exists(sanity_data.expand("${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}")):
                subprocess.call(sanity_data.expand("mv ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS} ${STAGING_BINDIR_CROSS}"), shell=True)
                subprocess.call(sanity_data.expand("ln -s ${STAGING_BINDIR_CROSS} ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}"), shell=True)

            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "4":
            messages = messages + "Staging layout has changed. The cross directory has been deprecated and cross packages are now built under the native sysroot.\nThis requires a rebuild.\n"
        elif abi == "5" and current_abi == "6":
            bb.note("Converting staging layout from version 5 to layout version 6")
            subprocess.call(sanity_data.expand("mv ${TMPDIR}/pstagelogs ${SSTATE_MANIFESTS}"), shell=True)
            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "7" and current_abi == "8":
            messages = messages + "Your configuration is using stamp files including the sstate hash but your build directory was built with stamp files that do not include this.\nTo continue, either rebuild or switch back to the OEBasic signature handler with BB_SIGNATURE_HANDLER = 'OEBasic'.\n"
        elif (abi != current_abi and current_abi == "9"):
            messages = messages + "The layout of the TMPDIR STAMPS directory has changed. Please clean out TMPDIR and rebuild (sstate will be still be valid and reused)\n"
        elif (abi != current_abi):
            # Code to convert from one ABI to another could go here if possible.
            messages = messages + "Error, TMPDIR has changed its layout version number (%s to %s) and you need to either rebuild, revert or adjust it at your own risk.\n" % (abi, current_abi)
    else:
        f = file(abifile, "w")
        f.write(current_abi)
    f.close()

    oeroot = sanity_data.getVar('COREBASE')
    if oeroot.find ('+') != -1:
        messages = messages + "Error, you have an invalid character (+) in your COREBASE directory path. Please move the installation to a directory which doesn't include a +."
    elif oeroot.find (' ') != -1:
        messages = messages + "Error, you have a space in your COREBASE directory path. Please move the installation to a directory which doesn't include a space."

    if messages != "":
        raise_sanity_error(sanity_data.expand(messages), sanity_data, network_error)
    return reparse

# Create a copy of the datastore and finalise it to ensure appends and 
# overrides are set - the datastore has yet to be finalised at ConfigParsed
def copy_data(e):
    sanity_data = bb.data.createCopy(e.data)
    sanity_data.finalize()
    return sanity_data

addhandler check_sanity_eventhandler
python check_sanity_eventhandler() {
    if bb.event.getName(e) == "ConfigParsed" and e.data.getVar("BB_WORKERCONTEXT", True) != "1" and e.data.getVar("DISABLE_SANITY_CHECKS", True) != "1":
        sanity_data = copy_data(e)
        reparse = check_sanity(sanity_data)
        e.data.setVar("BB_INVALIDCONF", reparse)
    elif bb.event.getName(e) == "SanityCheck":
        sanity_data = copy_data(e)
        sanity_data.setVar("SANITY_USE_EVENTS", "1")
        reparse = check_sanity(sanity_data)
        e.data.setVar("BB_INVALIDCONF", reparse)
        bb.event.fire(bb.event.SanityCheckPassed(), e.data)
    elif bb.event.getName(e) == "NetworkTest":
        sanity_data = copy_data(e)
        bb.event.fire(bb.event.NetworkTestFailed() if check_connectivity(sanity_data) else bb.event.NetworkTestPassed(), e.data)

    return
}
