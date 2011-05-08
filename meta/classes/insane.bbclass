# BB Class inspired by ebuild.sh
#
# This class will test files after installation for certain
# security issues and other kind of issues.
#
# Checks we do:
#  -Check the ownership and permissions
#  -Check the RUNTIME path for the $TMPDIR
#  -Check if .la files wrongly point to workdir
#  -Check if .pc files wrongly point to workdir
#  -Check if packages contains .debug directories or .so files
#   where they should be in -dev or -dbg
#  -Check if config.log contains traces to broken autoconf tests


#
# We need to have the scanelf utility as soon as
# possible and this is contained within the pax-utils-native.
# The package.bbclass can help us here.
#
inherit package
PACKAGE_DEPENDS += "pax-utils-native desktop-file-utils-native"
PACKAGEFUNCS += " do_package_qa "


#
# dictionary for elf headers
#
# feel free to add and correct.
#
#           TARGET_OS  TARGET_ARCH   MACHINE, OSABI, ABIVERSION, Little Endian, 32bit?
def package_qa_get_machine_dict():
    return {
            "darwin9" : { 
                        "arm" :       (40,     0,    0,          True,          32),
                      },
            "linux" : { 
                        "arm" :       (40,    97,    0,          True,          32),
                        "armeb":      (40,    97,    0,          False,         32),
                        "powerpc":    (20,     0,    0,          False,         32),
                        "i386":       ( 3,     0,    0,          True,          32),
                        "i486":       ( 3,     0,    0,          True,          32),
                        "i586":       ( 3,     0,    0,          True,          32),
                        "i686":       ( 3,     0,    0,          True,          32),
                        "x86_64":     (62,     0,    0,          True,          64),
                        "ia64":       (50,     0,    0,          True,          64),
                        "alpha":      (36902,  0,    0,          True,          64),
                        "hppa":       (15,     3,    0,          False,         32),
                        "m68k":       ( 4,     0,    0,          False,         32),
                        "mips":       ( 8,     0,    0,          False,         32),
                        "mipsel":     ( 8,     0,    0,          True,          32),
                        "s390":       (22,     0,    0,          False,         32),
                        "sh4":        (42,     0,    0,          True,          32),
                        "sparc":      ( 2,     0,    0,          False,         32),
                      },
            "linux-uclibc" : { 
                        "arm" :       (  40,    97,    0,          True,          32),
                        "armeb":      (  40,    97,    0,          False,         32),
                        "powerpc":    (  20,     0,    0,          False,         32),
                        "i386":       (   3,     0,    0,          True,          32),
                        "i486":       (   3,     0,    0,          True,          32),
                        "i586":       (   3,     0,    0,          True,          32),
                        "i686":       (   3,     0,    0,          True,          32),
                        "x86_64":     (  62,     0,    0,          True,          64),
                        "mips":       (   8,     0,    0,          False,         32),
                        "mipsel":     (   8,     0,    0,          True,          32),
                        "avr32":      (6317,     0,    0,          False,         32),
			"sh4":        (42,	 0,    0,          True,          32),

                      },
            "uclinux-uclibc" : {
                        "bfin":       ( 106,     0,    0,          True,         32),
                      }, 
            "linux-gnueabi" : {
                        "arm" :       (40,     0,    0,          True,          32),
                        "armeb" :     (40,     0,    0,          False,         32),
                      },
            "linux-uclibceabi" : {
                        "arm" :       (40,     0,    0,          True,          32),
                        "armeb" :     (40,     0,    0,          False,         32),
                      },
            "linux-gnuspe" : {
                        "powerpc":    (20,     0,    0,          False,         32),
                      },
            "linux-uclibcspe" : {
                        "powerpc":    (20,     0,    0,          False,         32),
                      },
            "linux-gnu" :       {
                        "microblaze": (47787,  0,    0,          False,         32),
                      },
       }


# Known Error classes
# 0 - non dev contains .so
# 1 - package contains a dangerous RPATH
# 2 - package depends on debug package
# 3 - non dbg contains .so
# 4 - wrong architecture
# 5 - .la contains installed=yes or reference to the workdir
# 6 - .pc contains reference to /usr/include or workdir
# 7 - the desktop file is not valid
# 8 - .la contains reference to the workdir
# 9 - LDFLAGS ignored
# 10 - Build paths in binaries

def package_qa_clean_path(path,d):
    """ Remove the common prefix from the path. In this case it is the TMPDIR"""
    return path.replace(bb.data.getVar('TMPDIR',d,True),"")

def package_qa_make_fatal_error(error_class, name, path,d):
    """
    decide if an error is fatal

    TODO: Load a whitelist of known errors
    """
    return False
    return not error_class in [0, 5, 7, 8, 9]

def package_qa_write_error(error_class, name, path, d):
    """
    Log the error
    """

    ERROR_NAMES =[
        "non dev contains .so",
        "package contains RPATH",
        "package depends on debug package",
        "non dbg contains .debug",
        "wrong architecture",
        "evil hides inside the .la",
        "evil hides inside the .pc",
        "the desktop file is not valid",
        ".la contains reference to the workdir",
        "LDFLAGS ignored",
        "package contains reference to tmpdir paths",
    ]

    log_path = os.path.join( bb.data.getVar('T', d, True), "log.qa_package" )
    f = file( log_path, "a+")
    print >> f, "%s, %s, %s" % \
             (ERROR_NAMES[error_class], name, package_qa_clean_path(path,d))
    f.close()

    logfile = bb.data.getVar('QA_LOGFILE', d, True)
    if logfile:
        p = bb.data.getVar('P', d, True)
        f = file( logfile, "a+")
        print >> f, "%s, %s, %s, %s" % \
             (p, ERROR_NAMES[error_class], name, package_qa_clean_path(path,d))
        f.close()

def package_qa_handle_error(error_class, error_msg, name, path, d):
    fatal = package_qa_make_fatal_error(error_class, name, path, d)
    if fatal:
        bb.error("QA Issue: %s" % error_msg)
    else:
        bb.warn("QA Issue: %s" % error_msg)
    package_qa_write_error(error_class, name, path, d)

    return not fatal

def package_qa_check_rpath(file,name,d, elf):
    """
    Check for dangerous RPATHs
    """
    if not elf:
        return True

    sane = True
    scanelf = os.path.join(bb.data.getVar('STAGING_BINDIR_NATIVE',d,True),'scanelf')
    bad_dirs = [bb.data.getVar('TMPDIR', d, True) + "/work", bb.data.getVar('STAGING_DIR_TARGET', d, True)]
    bad_dir_test = bb.data.getVar('TMPDIR', d, True)
    if not os.path.exists(scanelf):
        bb.fatal("Can not check RPATH, scanelf (part of pax-utils-native) not found")

    if not bad_dirs[0] in bb.data.getVar('WORKDIR', d, True):
        bb.fatal("This class assumed that WORKDIR is ${TMPDIR}/work... Not doing any check")

    output = os.popen("%s -B -F%%r#F '%s'" % (scanelf,file))
    txt    = output.readline().split()
    for line in txt:
        for dir in bad_dirs:
            if dir in line:
                error_msg = "package %s contains bad RPATH %s in file %s" % (name, line, file)
                sane = sane + package_qa_handle_error(1, error_msg, name, file, d)

    return sane

def package_qa_check_dev(path, name,d, elf):
    """
    Check for ".so" library symlinks in non-dev packages
    """

    sane = True

    if not name.endswith("-dev") and not name.endswith("-dbg") and path.endswith(".so") and os.path.islink(path):
        error_msg = "non -dev/-dbg package contains symlink .so: %s path '%s'" % \
                 (name, package_qa_clean_path(path,d))
        sane = package_qa_handle_error(0, error_msg, name, path, d)

    return sane

def package_qa_check_dbg(path, name,d, elf):
    """
    Check for ".debug" files or directories outside of the dbg package
    """

    sane = True

    if not "-dbg" in name:
        if '.debug' in path.split(os.path.sep):
            error_msg = "non debug package contains .debug directory: %s path %s" % \
                     (name, package_qa_clean_path(path,d))
            sane = package_qa_handle_error(3, error_msg, name, path, d)

    return sane

def package_qa_check_perm(path,name,d, elf):
    """
    Check the permission of files
    """
    sane = True
    return sane

def package_qa_check_arch(path,name,d, elf):
    """
    Check if archs are compatible
    """
    if not elf:
        return True

    sane = True
    target_os   = bb.data.getVar('TARGET_OS',   d, True)
    target_arch = bb.data.getVar('TARGET_ARCH', d, True)

    # FIXME: Cross package confuse this check, so just skip them
    for s in ['cross', 'nativesdk', 'cross-canadian']:
        if bb.data.inherits_class(s, d):
            return True

    # avoid following links to /usr/bin (e.g. on udev builds)
    # we will check the files pointed to anyway...
    if os.path.islink(path):
        return True

    #if this will throw an exception, then fix the dict above
    (machine, osabi, abiversion, littleendian, bits) \
        = package_qa_get_machine_dict()[target_os][target_arch]

    # Check the architecture and endiannes of the binary
    if not machine == elf.machine():
        error_msg = "Architecture did not match (%d to %d) on %s" % \
                 (machine, elf.machine(), package_qa_clean_path(path,d))
        sane = package_qa_handle_error(4, error_msg, name, path, d)
    elif not bits == elf.abiSize():
        error_msg = "Bit size did not match (%d to %d) on %s" % \
                 (bits, elf.abiSize(), package_qa_clean_path(path,d))
        sane = package_qa_handle_error(4, error_msg, name, path, d)
    elif not littleendian == elf.isLittleEndian():
        error_msg = "Endiannes did not match (%d to %d) on %s" % \
                 (littleendian, elf.isLittleEndian(), package_qa_clean_path(path,d))
        sane = package_qa_handle_error(4, error_msg, name, path, d)

    return sane

def package_qa_check_desktop(path, name, d, elf):
    """
    Run all desktop files through desktop-file-validate.
    """
    sane = True
    if path.endswith(".desktop"):
        desktop_file_validate = os.path.join(bb.data.getVar('STAGING_BINDIR_NATIVE',d,True),'desktop-file-validate')
        output = os.popen("%s %s" % (desktop_file_validate, path))
        # This only produces output on errors
        for l in output:
            sane = package_qa_handle_error(7, l.strip(), name, path, d)

    return sane

def package_qa_hash_style(path, name, d, elf):
    """
    Check if the binary has the right hash style...
    """

    if not elf:
        return True

    if os.path.islink(path):
        return True

    gnu_hash = "--hash-style=gnu" in bb.data.getVar('LDFLAGS', d, True)
    if not gnu_hash:
        gnu_hash = "--hash-style=both" in bb.data.getVar('LDFLAGS', d, True)
    if not gnu_hash:
        return True

    objdump = bb.data.getVar('OBJDUMP', d, True)
    env_path = bb.data.getVar('PATH', d, True)

    sane = True
    elf = False
    # A bit hacky. We do not know if path is an elf binary or not
    # we will search for 'NEEDED' or 'INIT' as this should be printed...
    # and come before the HASH section (guess!!!) and works on split out
    # debug symbols too
    for line in os.popen("LC_ALL=C PATH=%s %s -p '%s' 2> /dev/null" % (env_path, objdump, path), "r"):
        if "NEEDED" in line or "INIT" in line:
            sane = False
            elf = True
        if "GNU_HASH" in line:
            sane = True
        if "[mips32]" in line or "[mips64]" in line:
	    sane = True

    if elf and not sane:
        error_msg = "No GNU_HASH in the elf binary: '%s'" % path
        return package_qa_handle_error(9, error_msg, name, path, d)

    return True

def package_qa_check_buildpaths(path, name, d, elf):
    """
    Check for build paths inside target files and error if not found in the whitelist
    """
    sane = True

    # Ignore .debug files, not interesting
    if path.find(".debug") != -1:
        return True

    # Ignore symlinks
    if os.path.islink(path):
        return True

    tmpdir = bb.data.getVar('TMPDIR', d, True)
    file_content = open(path).read()
    if tmpdir in file_content:
        error_msg = "File %s in package contained reference to tmpdir" % package_qa_clean_path(path,d)
        sane = package_qa_handle_error(10, error_msg, name, path, d)
    return sane

def package_qa_check_license(workdir, d):
    """
    Check for changes in the license files 
    """
    import tempfile
    sane = True

    lic_files = bb.data.getVar('LIC_FILES_CHKSUM', d, True)
    lic = bb.data.getVar('LICENSE', d, True)
    pn = bb.data.getVar('PN', d, True)

    if lic == "CLOSED":
        return True

    if not lic_files:
        # just throw a warning now. Once licensing data in entered for enough of the recipes,
        # this will be converted into error and False will be returned.
        bb.error(pn + ": Recipe file does not have license file information (LIC_FILES_CHKSUM)")
        return False

    srcdir = bb.data.getVar('S', d, True)

    for url in lic_files.split():
        (type, host, path, user, pswd, parm) = bb.decodeurl(url)
        srclicfile = os.path.join(srcdir, path)
        if not os.path.isfile(srclicfile):
            raise bb.build.FuncFailed( pn + ": LIC_FILES_CHKSUM points to invalid file: " + path)

        if 'md5' not in parm:
            bb.error(pn + ": md5 checksum is not specified for ", url)
            return False
        beginline, endline = 0, 0
        if 'beginline' in parm:
            beginline = int(parm['beginline'])
        if 'endline' in parm:
            endline = int(parm['endline'])

        if (not beginline) and (not endline):
            md5chksum = bb.utils.md5_file(srclicfile)
        else:
            fi = open(srclicfile, 'r')
            fo = tempfile.NamedTemporaryFile(mode='wb', prefix='poky.', suffix='.tmp', delete=False)
            tmplicfile = fo.name;
            lineno = 0
            linesout = 0
            for line in fi:
                lineno += 1
                if (lineno >= beginline): 
                    if ((lineno <= endline) or not endline):
                        fo.write(line)
                        linesout += 1
                    else:
                        break
            fo.flush()
            fo.close()
            fi.close()
            md5chksum = bb.utils.md5_file(tmplicfile)
            os.unlink(tmplicfile)

        if parm['md5'] == md5chksum:
            bb.note (pn + ": md5 checksum matched for ", url)
        else:
            bb.error (pn + ": md5 data is not matching for ", url)
            bb.error (pn + ": The new md5 checksum is ", md5chksum)
            bb.error (pn + ": Check if the license information has changed in")
            sane = False

    return sane

def package_qa_check_staged(path,d):
    """
    Check staged la and pc files for sanity
      -e.g. installed being false

        As this is run after every stage we should be able
        to find the one responsible for the errors easily even
        if we look at every .pc and .la file
    """

    sane = True
    tmpdir = bb.data.getVar('TMPDIR', d, True)
    workdir = os.path.join(tmpdir, "work")

    installed = "installed=yes"
    if bb.data.inherits_class("native", d) or bb.data.inherits_class("cross", d):
        pkgconfigcheck = workdir
    else:
        pkgconfigcheck = tmpdir

    # find all .la and .pc files
    # read the content
    # and check for stuff that looks wrong
    for root, dirs, files in os.walk(path):
        for file in files:
            path = os.path.join(root,file)
            if file.endswith(".la"):
                file_content = open(path).read()
                if workdir in file_content:
                    error_msg = "%s failed sanity test (workdir) in path %s" % (file,root)
                    sane = package_qa_handle_error(8, error_msg, "staging", path, d)
            elif file.endswith(".pc"):
                file_content = open(path).read()
                if pkgconfigcheck in file_content:
                    error_msg = "%s failed sanity test (tmpdir) in path %s" % (file,root)
                    sane = package_qa_handle_error(6, error_msg, "staging", path, d)

    return sane

# Walk over all files in a directory and call func
def package_qa_walk(path, funcs, package,d):
    import oe.qa

    #if this will throw an exception, then fix the dict above
    target_os   = bb.data.getVar('TARGET_OS',   d, True)
    target_arch = bb.data.getVar('TARGET_ARCH', d, True)

    sane = True
    for root, dirs, files in os.walk(path):
        for file in files:
            path = os.path.join(root,file)
            elf = oe.qa.ELFFile(path)
            try:
                elf.open()
            except:
                elf = None
            for func in funcs:
                if not func(path, package,d, elf):
                    sane = False

    return sane

def package_qa_check_rdepends(pkg, pkgdest, d):
    sane = True
    if not "-dbg" in pkg and not "task-" in pkg and not "-image" in pkg:
        # Copied from package_ipk.bbclass
        # boiler plate to update the data
        localdata = bb.data.createCopy(d)
        root = "%s/%s" % (pkgdest, pkg)

        bb.data.setVar('ROOT', '', localdata) 
        bb.data.setVar('ROOT_%s' % pkg, root, localdata)
        pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, True)
        if not pkgname:
            pkgname = pkg
        bb.data.setVar('PKG', pkgname, localdata)

        bb.data.setVar('OVERRIDES', pkg, localdata)

        bb.data.update_data(localdata)

        # Now check the RDEPENDS
        rdepends = bb.utils.explode_deps(bb.data.getVar('RDEPENDS', localdata, True) or "")


        # Now do the sanity check!!!
        for rdepend in rdepends:
            if "-dbg" in rdepend:
                error_msg = "%s rdepends on %s" % (pkgname,rdepend)
                sane = package_qa_handle_error(2, error_msg, pkgname, rdepend, d)

    return sane

# The PACKAGE FUNC to scan each package
python do_package_qa () {
    bb.note("DO PACKAGE QA")

    logdir = bb.data.getVar('T', d, True)
    pkg = bb.data.getVar('PN', d, True)

    # Check the compile log for host contamination
    compilelog = os.path.join(logdir,"log.do_compile")

    statement = "grep -e 'CROSS COMPILE Badness:' -e 'is unsafe for cross-compilation' %s > /dev/null" % compilelog
    if os.system(statement) == 0:
        bb.warn("%s: The compile log indicates that host include and/or library paths were used.  Please check the log '%s' for more information." % \
                (pkg, compilelog))


    # Check the install log for host contamination
    installlog = os.path.join(logdir,"log.do_install")

    statement = "grep -e 'CROSS COMPILE Badness:' -e 'is unsafe for cross-compilation' %s > /dev/null" % installlog
    if os.system(statement) == 0:
        bb.warn("%s: The install log indicates that host include and/or library paths were used.  Please check the log '%s' for more information." % \
                (pkg, installlog))

    # Scan the packages...
    pkgdest = bb.data.getVar('PKGDEST', d, True)
    packages = bb.data.getVar('PACKAGES',d, True)

    # no packages should be scanned
    if not packages:
        return

    checks = [package_qa_check_rpath, package_qa_check_dev,
              package_qa_check_perm, package_qa_check_arch,
              package_qa_check_desktop, package_qa_hash_style,
              package_qa_check_dbg]
    #         package_qa_check_buildpaths, 
    walk_sane = True
    rdepends_sane = True
    for package in packages.split():
        if bb.data.getVar('INSANE_SKIP_' + package, d, True):
            bb.note("Package: %s (skipped)" % package)
            continue

        bb.note("Checking Package: %s" % package)
        path = "%s/%s" % (pkgdest, package)
        if not package_qa_walk(path, checks, package, d):
            walk_sane  = False
        if not package_qa_check_rdepends(package, pkgdest, d):
            rdepends_sane = False


    if not walk_sane or not rdepends_sane:
        bb.fatal("QA run found fatal errors. Please consider fixing them.")
    bb.note("DONE with PACKAGE QA")
}


python do_qa_staging() {
    bb.note("QA checking staging")

    if not package_qa_check_staged(bb.data.expand('${SYSROOT_DESTDIR}/${STAGING_LIBDIR}',d), d):
        bb.fatal("QA staging was broken by the package built above")
}

python do_qa_configure() {
    configs = []
    workdir = bb.data.getVar('WORKDIR', d, True)
    bb.note("Checking autotools environment for common misconfiguration")
    for root, dirs, files in os.walk(workdir):
        statement = "grep -e 'CROSS COMPILE Badness:' -e 'is unsafe for cross-compilation' %s > /dev/null" % \
                    os.path.join(root,"config.log")
        if "config.log" in files:
            if os.system(statement) == 0:
                bb.fatal("""This autoconf log indicates errors, it looked at host include and/or library paths while determining system capabilities.
Rerun configure task after fixing this. The path was '%s'""" % root)

        if "configure.ac" in files:
            configs.append(os.path.join(root,"configure.ac"))
        if "configure.in" in files:
            configs.append(os.path.join(root, "configure.in"))

    cnf = bb.data.getVar('EXTRA_OECONF', d, True) or ""
    if "gettext" not in bb.data.getVar('P', d, True) and "gcc-runtime" not in bb.data.getVar('P', d, True) and "--disable-nls" not in cnf:
       if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('nativesdk', d):
          gt = "gettext-native"
       elif bb.data.inherits_class('cross-canadian', d):
          gt = "gettext-nativesdk"
       else:
          gt = "virtual/gettext"
       deps = bb.utils.explode_deps(bb.data.getVar('DEPENDS', d, True) or "")
       if gt not in deps:
          for config in configs:
              gnu = "grep \"^[[:space:]]*AM_GNU_GETTEXT\" %s >/dev/null" % config
              if os.system(gnu) == 0:
                 bb.fatal("""%s required but not in DEPENDS for file %s.
Missing inherit gettext?""" % (gt, config))

    if not package_qa_check_license(workdir, d):
        bb.fatal("Licensing Error: LIC_FILES_CHKSUM does not match, please fix")
}
# The Staging Func, to check all staging
#addtask qa_staging after do_populate_sysroot before do_build
do_populate_sysroot[postfuncs] += "do_qa_staging "

# Check broken config.log files, for packages requiring Gettext which don't
# have it in DEPENDS and for correct LIC_FILES_CHKSUM
#addtask qa_configure after do_configure before do_compile
do_configure[postfuncs] += "do_qa_configure "
