#
# Sanity check the users setup for common misconfigurations
#

SANITY_REQUIRED_UTILITIES ?= "patch diffstat texi2html makeinfo cvs svn bzip2 tar gzip gawk chrpath wget cpio"

def raise_sanity_error(msg):
    bb.fatal(""" Poky's config sanity checker detected a potential misconfiguration.
    Either fix the cause of this error or at your own risk disable the checker (see sanity.conf).
    Following is the list of potential problems / advisories:
    
    %s""" % msg)

def check_conf_exists(fn, data):
    bbpath = []
    fn = bb.data.expand(fn, data)
    vbbpath = bb.data.getVar("BBPATH", data)
    if vbbpath:
        bbpath += vbbpath.split(":")
    for p in bbpath:
        currname = os.path.join(bb.data.expand(p, data), fn)
        if os.access(currname, os.R_OK):
            return True
    return False

def check_sanity_sstate_dir_change(sstate_dir, data):
    # Sanity checks to be done when the value of SSTATE_DIR changes

    # Check that SSTATE_DIR isn't on a filesystem with limited filename length (eg. eCryptFS)
    testmsg = ""
    if sstate_dir != "":
        testmsg = check_create_long_filename(sstate_dir, "SSTATE_DIR")
    return testmsg

def check_sanity_tmpdir_change(tmpdir, data):
    # Sanity checks to be done when the value of TMPDIR changes

    # Check that TMPDIR isn't on a filesystem with limited filename length (eg. eCryptFS)
    testmsg = check_create_long_filename(tmpdir, "TMPDIR")
    # Check that we can fetch from various network transports
    testmsg = testmsg + check_connectivity(data)
    return testmsg
        
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
            return "Failed to create a file in %s: %s" % (pathname, strerror)
    return ""

def check_connectivity(d):
    # URI's to check can be set in the CONNECTIVITY_CHECK_URIS variable
    # using the same syntax as for SRC_URI. If the variable is not set
    # the check is skipped
    test_uris = (bb.data.getVar('CONNECTIVITY_CHECK_URIS', d, True) or "").split()
    retval = ""

    # Only check connectivity if network enabled and the
    # CONNECTIVITY_CHECK_URIS are set
    network_enabled = not bb.data.getVar('BB_NO_NETWORK', d, True)
    check_enabled = len(test_uris)
    if check_enabled and network_enabled:
        data = bb.data.createCopy(d)
        bookmark = os.getcwd()
        dldir = bb.data.expand('${TMPDIR}/sanity', data)
        bb.data.setVar('DL_DIR', dldir, data)

        try:
            fetcher = bb.fetch2.Fetch(test_uris, data)
            fetcher.download()
            fetcher.clean(test_uris)
        except Exception:
            # Allow the message to be configured so that users can be
            # pointed to a support mechanism.
            msg = bb.data.getVar('CONNECTIVITY_CHECK_MSG', d, True) or ""
            if len(msg) == 0:
                msg = "Failed to fetch test data from the network. Please ensure your network is configured correctly.\n"
            retval = msg
        finally:
            # Make sure we tidy up the cruft
            oe.path.remove(dldir)
            os.chdir(bookmark)

    return retval

def check_sanity(e):
    from bb import note, error, data, __version__

    try:
        from distutils.version import LooseVersion
    except ImportError:
        def LooseVersion(v): print "WARNING: sanity.bbclass can't compare versions without python-distutils"; return 1
    import commands

    # Check the bitbake version meets minimum requirements
    minversion = data.getVar('BB_MIN_VERSION', e.data , True)
    if not minversion:
        # Hack: BB_MIN_VERSION hasn't been parsed yet so return 
        # and wait for the next call
        print "Foo %s" % minversion
        return

    if 0 == os.getuid():
        raise_sanity_error("Do not use Bitbake as root.")

    messages = ""

    # Check the Python version, we now use Python 2.6 features in
    # various classes
    import sys
    if sys.hexversion < 0x020600F0:
        messages = messages + 'The system requires at least Python 2.6 to run. Please update your Python interpreter.\n'

    if (LooseVersion(__version__) < LooseVersion(minversion)):
        messages = messages + 'Bitbake version %s is required and version %s was found\n' % (minversion, __version__)

    # Check TUNE_ARCH is set
    if data.getVar('TUNE_ARCH', e.data, True) == 'INVALID':
        messages = messages + 'TUNE_ARCH is unset. Please ensure your MACHINE configuration includes a valid tune configuration file which will set this correctly.\n'

    # Check TARGET_ARCH is set correctly
    if data.getVar('TARGE_ARCH', e.data, False) == '${TUNE_ARCH}':
        messages = messages + 'TARGET_ARCH is being overwritten, likely by your MACHINE configuration files.\nPlease use a valid tune configuration file which should set this correctly automatically\nand avoid setting this in the machine configuration. See the OE-Core mailing list for more information.\n'
    
    # Check TARGET_OS is set
    if data.getVar('TARGET_OS', e.data, True) == 'INVALID':
        messages = messages + 'Please set TARGET_OS directly, or choose a MACHINE or DISTRO that does so.\n'

        # Check we are using a valid lacal.conf
        current_conf  = data.getVar('CONF_VERSION', e.data, True)
        conf_version =  data.getVar('LOCALCONF_VERSION', e.data, True)

        if current_conf != conf_version:
            messages = messages + "Your version of local.conf was generated from an older version of local.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/local.conf conf/local.conf.sample\" is a good way to visualise the changes.\n"

        # Check bblayers.conf is valid
        current_lconf = data.getVar('LCONF_VERSION', e.data, True)
        lconf_version = data.getVar('LAYER_CONF_VERSION', e.data, True)
        if current_lconf != lconf_version:
            messages = messages + "Your version of bblayers.conf was generated from an older version of bblayers.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/bblayers.conf conf/bblayers.conf.sample\" is a good way to visualise the changes.\n"

        # If we have a site.conf, check it's valid
        if check_conf_exists("conf/site.conf", e.data):
            current_sconf = data.getVar('SCONF_VERSION', e.data, True)
            sconf_version = data.getVar('SITE_CONF_VERSION', e.data, True)
            if current_sconf != sconf_version:
                messages = messages + "Your version of site.conf was generated from an older version of site.conf.sample and there have been updates made to this file. Please compare the two files and merge any changes before continuing.\nMatching the version numbers will remove this message.\n\"meld conf/site.conf conf/site.conf.sample\" is a good way to visualise the changes.\n"

    assume_provided = data.getVar('ASSUME_PROVIDED', e.data , True).split()
    # Check user doesn't have ASSUME_PROVIDED = instead of += in local.conf
    if "diffstat-native" not in assume_provided:
        messages = messages + 'Please use ASSUME_PROVIDED +=, not ASSUME_PROVIDED = in your local.conf\n'
    
    # Check that the MACHINE is valid, if it is set
    if data.getVar('MACHINE', e.data, True):
        if not check_conf_exists("conf/machine/${MACHINE}.conf", e.data):
            messages = messages + 'Please set a valid MACHINE in your local.conf\n'

    # Check that DL_DIR is set, exists and is writable. In theory, we should never even hit the check if DL_DIR isn't 
    # set, since so much relies on it being set.
    dldir = data.getVar('DL_DIR', e.data, True)
    if not dldir:
        messages = messages + "DL_DIR is not set. Your environment is misconfigured, check that DL_DIR is set, and if the directory exists, that it is writable. \n"
    if os.path.exists(dldir) and not os.access(dldir, os.W_OK):
        messages = messages + "DL_DIR: %s exists but you do not appear to have write access to it. \n" % dldir
    
    # Check that the DISTRO is valid
    # need to take into account DISTRO renaming DISTRO
    #if not ( check_conf_exists("conf/distro/${DISTRO}.conf", e.data) or check_conf_exists("conf/distro/include/${DISTRO}.inc", e.data) ):
    #    messages = messages + "DISTRO '%s' not found. Please set a valid DISTRO in your local.conf\n" % data.getVar("DISTRO", e.data, True )

    missing = ""

    if not check_app_exists("${MAKE}", e.data):
        missing = missing + "GNU make,"

    if not check_app_exists('${BUILD_PREFIX}gcc', e.data):
        missing = missing + "C Compiler (%sgcc)," % data.getVar("BUILD_PREFIX", e.data, True)

    if not check_app_exists('${BUILD_PREFIX}g++', e.data):
        missing = missing + "C++ Compiler (%sg++)," % data.getVar("BUILD_PREFIX", e.data, True)

    required_utilities = e.data.getVar('SANITY_REQUIRED_UTILITIES', True)

    if "qemu-native" in assume_provided:
        if not check_app_exists("qemu-arm", e.data):
            messages = messages + "qemu-native was in ASSUME_PROVIDED but the QEMU binaries (qemu-arm) can't be found in PATH"

    if "." in data.getVar('PATH', e.data, True).split(":"):
        messages = messages + "PATH contains '.' which will break the build, please remove this"

    if data.getVar('TARGET_ARCH', e.data, True) == "arm":
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
        if not check_app_exists( util, e.data ):
            missing = missing + "%s," % util

    if missing != "":
        missing = missing.rstrip(',')
        messages = messages + "Please install following missing utilities: %s\n" % missing

    pseudo_msg = check_pseudo_wrapper()
    if pseudo_msg != "":
        messages = messages + pseudo_msg + '\n'

    # Check if DISPLAY is set if IMAGETEST is set
    if not data.getVar( 'DISPLAY', e.data, True ) and data.getVar( 'IMAGETEST', e.data, True ) == 'qemu':
        messages = messages + 'qemuimagetest needs a X desktop to start qemu, please set DISPLAY correctly (e.g. DISPLAY=:1.0)\n'

    if data.getVar('PATCHRESOLVE', e.data, True) != 'noop':
        # Ensure we have the binary for TERMCMD, as when patch application fails the error is fairly intimidating
        termcmd = data.getVar("TERMCMD", e.data, True)
        term = termcmd.split()[0]
        if not check_app_exists(term, e.data):
            messages = messages + "The console for use in patch error resolution is not available, please install %s or set TERMCMD and TERMCMDRUN (as documented in local.conf).\n" % term

    if os.path.basename(os.readlink('/bin/sh')) == 'dash':
        messages = messages + "Using dash as /bin/sh causes various subtle build problems, please use bash instead (e.g. 'dpkg-reconfigure dash' on an Ubuntu system.\n"

    omask = os.umask(022)
    if omask & 0755:
        messages = messages + "Please use a umask which allows a+rx and u+rwx\n"
    os.umask(omask)

    oes_bb_conf = data.getVar( 'OES_BITBAKE_CONF', e.data, True )
    if not oes_bb_conf:
        messages = messages + 'You do not include OpenEmbeddeds version of conf/bitbake.conf. This means your environment is misconfigured, in particular check BBPATH.\n'

    nolibs = data.getVar('NO32LIBS', e.data, True)
    if not nolibs:
        lib32path = '/lib'
        if os.path.exists('/lib64') and ( os.path.islink('/lib64') or os.path.islink('/lib') ):
           lib32path = '/lib32'

        if os.path.exists('%s/libc.so.6' % lib32path) and not os.path.exists('/usr/include/gnu/stubs-32.h'):
            messages = messages + "You have a 32-bit libc, but no 32-bit headers.  You must install the 32-bit libc headers.\n"

    tmpdir = data.getVar('TMPDIR', e.data, True)
    sstate_dir = data.getVar('SSTATE_DIR', e.data, True)

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
    
    sanity_version = int(data.getVar('SANITY_VERSION', e.data, True) or 1)
    if last_sanity_version < sanity_version: 
        messages = messages + check_sanity_version_change(e.data)
        messages = messages + check_sanity_tmpdir_change(tmpdir, e.data)
        messages = messages + check_sanity_sstate_dir_change(sstate_dir, e.data)
    else: 
        if last_tmpdir != tmpdir:
            messages = messages + check_sanity_tmpdir_change(tmpdir, e.data)
        if last_sstate_dir != sstate_dir:
            messages = messages + check_sanity_sstate_dir_change(sstate_dir, e.data)

    if os.path.exists("conf"):
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
        f = file(checkfile, "w")
        f.write(tmpdir)
    f.close()

    #
    # Check the 'ABI' of TMPDIR
    #
    current_abi = data.getVar('OELAYOUT_ABI', e.data, True)
    abifile = data.getVar('SANITY_ABIFILE', e.data, True)
    if os.path.exists(abifile):
        f = file(abifile, "r")
        abi = f.read().strip()
        if not abi.isdigit():
            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "2" and current_abi == "3":
            bb.note("Converting staging from layout version 2 to layout version 3")
            os.system(bb.data.expand("mv ${TMPDIR}/staging ${TMPDIR}/sysroots", e.data))
            os.system(bb.data.expand("ln -s sysroots ${TMPDIR}/staging", e.data))
            os.system(bb.data.expand("cd ${TMPDIR}/stamps; for i in */*do_populate_staging; do new=`echo $i | sed -e 's/do_populate_staging/do_populate_sysroot/'`; mv $i $new; done", e.data))
            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "3" and current_abi == "4":
            bb.note("Converting staging layout from version 3 to layout version 4")
            if os.path.exists(bb.data.expand("${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}", e.data)):
                os.system(bb.data.expand("mv ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS} ${STAGING_BINDIR_CROSS}", e.data))
                os.system(bb.data.expand("ln -s ${STAGING_BINDIR_CROSS} ${STAGING_DIR_NATIVE}${bindir_native}/${MULTIMACH_HOST_SYS}", e.data))

            f = file(abifile, "w")
            f.write(current_abi)
        elif abi == "4":
            messages = messages + "Staging layout has changed. The cross directory has been deprecated and cross packages are now built under the native sysroot.\nThis requires a rebuild.\n"
        elif abi == "5" and current_abi == "6":
            bb.note("Converting staging layout from version 5 to layout version 6")
            os.system(bb.data.expand("mv ${TMPDIR}/pstagelogs ${SSTATE_MANIFESTS}", e.data))
            f = file(abifile, "w")
            f.write(current_abi)
        elif (abi != current_abi):
            # Code to convert from one ABI to another could go here if possible.
            messages = messages + "Error, TMPDIR has changed its layout version number (%s to %s) and you need to either rebuild, revert or adjust it at your own risk.\n" % (abi, current_abi)
    else:
        f = file(abifile, "w")
        f.write(current_abi)
    f.close()

    oeroot = data.getVar('COREBASE', e.data)
    if oeroot.find ('+') != -1:
        messages = messages + "Error, you have an invalid character (+) in your COREBASE directory path. Please move the installation to a directory which doesn't include a +."
    elif oeroot.find (' ') != -1:
        messages = messages + "Error, you have a space in your COREBASE directory path. Please move the installation to a directory which doesn't include a space."

    # Check that we don't have duplicate entries in PACKAGE_ARCHS & that TUNE_PKGARCH is in PACKAGE_ARCHS
    pkgarchs = data.getVar('PACKAGE_ARCHS', e.data, True)
    tunepkg = data.getVar('TUNE_PKGARCH', e.data, True)
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

    if messages != "":
        raise_sanity_error(messages)

addhandler check_sanity_eventhandler
python check_sanity_eventhandler() {
    if bb.event.getName(e) == "ConfigParsed" and bb.data.getVar("BB_WORKERCONTEXT", e.data, True) != "1":
        check_sanity(e)

    return
}
