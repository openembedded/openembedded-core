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
                        "powerpc64":  (21,     0,    0,          False,         64),
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
            "linux-gnu" : {
                        "powerpc":    (20,     0,    0,          False,         32),
                      },
            "linux-gnuspe" : {
                        "powerpc":    (20,     0,    0,          False,         32),
                      },
            "linux-uclibcspe" : {
                        "powerpc":    (20,     0,    0,          False,         32),
                      },
            "linux-gnu" :       {
                        "microblaze":   (47787,  0,    0,          False,         32),
                        "microblazeel": (47787,  0,    0,          True,          32),
                      },
            "linux-gnux32" :       {
                        "x86_64":     (62,     0,    0,          True,          32),
                      },
       }


WARN_QA ?= "dev-so rpaths debug-deps dev-deps debug-files arch la2 pkgconfig desktop la ldflags perms useless-rpaths"
ERROR_QA ?= ""
#ERROR_QA ?= "rpaths debug-deps dev-deps debug-files arch pkgconfig perms"

def package_qa_clean_path(path,d):
    """ Remove the common prefix from the path. In this case it is the TMPDIR"""
    return path.replace(bb.data.getVar('TMPDIR',d,True),"")

def package_qa_write_error(error, d):
    logfile = d.getVar('QA_LOGFILE', True)
    if logfile:
        p = d.getVar('P', True)
        f = file( logfile, "a+")
        print >> f, "%s: %s" % (p, error)
        f.close()

def package_qa_handle_error(error_class, error_msg, d):
    package_qa_write_error(error_msg, d)
    if error_class in (d.getVar("ERROR_QA", True) or "").split():
        bb.error("QA Issue: %s" % error_msg)
        return False
    else:
        bb.warn("QA Issue: %s" % error_msg)
        return True

QAPATHTEST[rpaths] = "package_qa_check_rpath"
def package_qa_check_rpath(file,name, d, elf, messages):
    """
    Check for dangerous RPATHs
    """
    if not elf:
        return

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
                messages.append("package %s contains bad RPATH %s in file %s" % (name, line, file))

QAPATHTEST[useless-rpaths] = "package_qa_check_useless_rpaths"
def package_qa_check_useless_rpaths(file, name, d, elf, messages):
    """
    Check for RPATHs that are useless but not dangerous
    """
    if not elf:
        return

    objdump = bb.data.getVar('OBJDUMP', d, True)
    env_path = bb.data.getVar('PATH', d, True)

    libdir = bb.data.getVar("libdir", d, True)
    base_libdir = bb.data.getVar("base_libdir", d, True)

    import re
    rpath_re = re.compile("\s+RPATH\s+(.*)")
    for line in os.popen("LC_ALL=C PATH=%s %s -p '%s' 2> /dev/null" % (env_path, objdump, file), "r"):
    	m = rpath_re.match(line)
	if m:
	   rpath = m.group(1)
	   if rpath == libdir or rpath == base_libdir:
	      # The dynamic linker searches both these places anyway.  There is no point in
	      # looking there again.
	      messages.append("%s: %s contains probably-redundant RPATH %s" % (name, package_qa_clean_path(file, d), rpath))

QAPATHTEST[dev-so] = "package_qa_check_dev"
def package_qa_check_dev(path, name, d, elf, messages):
    """
    Check for ".so" library symlinks in non-dev packages
    """

    if not name.endswith("-dev") and not name.endswith("-dbg") and path.endswith(".so") and os.path.islink(path):
        messages.append("non -dev/-dbg package contains symlink .so: %s path '%s'" % \
                 (name, package_qa_clean_path(path,d)))

QAPATHTEST[debug-files] = "package_qa_check_dbg"
def package_qa_check_dbg(path, name, d, elf, messages):
    """
    Check for ".debug" files or directories outside of the dbg package
    """

    if not "-dbg" in name:
        if '.debug' in path.split(os.path.sep):
            messages.append("non debug package contains .debug directory: %s path %s" % \
                     (name, package_qa_clean_path(path,d)))

QAPATHTEST[perms] = "package_qa_check_perm"
def package_qa_check_perm(path,name,d, elf, messages):
    """
    Check the permission of files
    """
    return

QAPATHTEST[arch] = "package_qa_check_arch"
def package_qa_check_arch(path,name,d, elf, messages):
    """
    Check if archs are compatible
    """
    if not elf:
        return

    target_os   = bb.data.getVar('TARGET_OS',   d, True)
    target_arch = bb.data.getVar('TARGET_ARCH', d, True)

    # FIXME: Cross package confuse this check, so just skip them
    for s in ['cross', 'nativesdk', 'cross-canadian']:
        if bb.data.inherits_class(s, d):
            return

    # avoid following links to /usr/bin (e.g. on udev builds)
    # we will check the files pointed to anyway...
    if os.path.islink(path):
        return

    #if this will throw an exception, then fix the dict above
    (machine, osabi, abiversion, littleendian, bits) \
        = package_qa_get_machine_dict()[target_os][target_arch]

    # Check the architecture and endiannes of the binary
    if not machine == elf.machine():
        messages.append("Architecture did not match (%d to %d) on %s" % \
                 (machine, elf.machine(), package_qa_clean_path(path,d)))
    elif not bits == elf.abiSize():
        messages.append("Bit size did not match (%d to %d) on %s" % \
                 (bits, elf.abiSize(), package_qa_clean_path(path,d)))
    elif not littleendian == elf.isLittleEndian():
        messages.append("Endiannes did not match (%d to %d) on %s" % \
                 (littleendian, elf.isLittleEndian(), package_qa_clean_path(path,d)))

QAPATHTEST[desktop] = "package_qa_check_desktop"
def package_qa_check_desktop(path, name, d, elf, messages):
    """
    Run all desktop files through desktop-file-validate.
    """
    if path.endswith(".desktop"):
        desktop_file_validate = os.path.join(bb.data.getVar('STAGING_BINDIR_NATIVE',d,True),'desktop-file-validate')
        output = os.popen("%s %s" % (desktop_file_validate, path))
        # This only produces output on errors
        for l in output:
            messages.append("Desktop file issue: " + l.strip())

QAPATHTEST[ldflags] = "package_qa_hash_style"
def package_qa_hash_style(path, name, d, elf, messages):
    """
    Check if the binary has the right hash style...
    """

    if not elf:
        return

    if os.path.islink(path):
        return

    gnu_hash = "--hash-style=gnu" in bb.data.getVar('LDFLAGS', d, True)
    if not gnu_hash:
        gnu_hash = "--hash-style=both" in bb.data.getVar('LDFLAGS', d, True)
    if not gnu_hash:
        return

    objdump = bb.data.getVar('OBJDUMP', d, True)
    env_path = bb.data.getVar('PATH', d, True)

    sane = False
    has_syms = False

    # If this binary has symbols, we expect it to have GNU_HASH too.
    for line in os.popen("LC_ALL=C PATH=%s %s -p '%s' 2> /dev/null" % (env_path, objdump, path), "r"):
        if "SYMTAB" in line:
            has_syms = True
        if "GNU_HASH" in line:
            sane = True
        if "[mips32]" in line or "[mips64]" in line:
	    sane = True

    if has_syms and not sane:
        messages.append("No GNU_HASH in the elf binary: '%s'" % path)


QAPATHTEST[buildpaths] = "package_qa_check_buildpaths"
def package_qa_check_buildpaths(path, name, d, elf, messages):
    """
    Check for build paths inside target files and error if not found in the whitelist
    """
    # Ignore .debug files, not interesting
    if path.find(".debug") != -1:
        return

    # Ignore symlinks
    if os.path.islink(path):
        return

    tmpdir = bb.data.getVar('TMPDIR', d, True)
    file_content = open(path).read()
    if tmpdir in file_content:
        messages.append("File %s in package contained reference to tmpdir" % package_qa_clean_path(path,d))

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
                    sane = package_qa_handle_error("la", error_msg, d)
            elif file.endswith(".pc"):
                file_content = open(path).read()
                if pkgconfigcheck in file_content:
                    error_msg = "%s failed sanity test (tmpdir) in path %s" % (file,root)
                    sane = package_qa_handle_error("pkgconfig", error_msg, d)

    return sane

# Walk over all files in a directory and call func
def package_qa_walk(path, warnfuncs, errorfuncs, skip, package, d):
    import oe.qa

    #if this will throw an exception, then fix the dict above
    target_os   = bb.data.getVar('TARGET_OS',   d, True)
    target_arch = bb.data.getVar('TARGET_ARCH', d, True)

    warnings = []
    errors = []
    for root, dirs, files in os.walk(path):
        for file in files:
            path = os.path.join(root,file)
            elf = oe.qa.ELFFile(path)
            try:
                elf.open()
            except:
                elf = None
            for func in warnfuncs:
                func(path, package, d, elf, warnings)
            for func in errorfuncs:
                func(path, package, d, elf, errors)

    for w in warnings:
        bb.warn("QA Issue: %s" % w)
        package_qa_write_error(w, d)
    for e in errors:
        bb.error("QA Issue: %s" % e)
        package_qa_write_error(e, d)

    return len(errors) == 0

def package_qa_check_rdepends(pkg, pkgdest, skip, d):
    # Don't do this check for kernel/module recipes, there aren't too many debug/development
    # packages and you can get false positives e.g. on kernel-module-lirc-dev
    if bb.data.inherits_class("kernel", d) or bb.data.inherits_class("module-base", d):
        return True

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
            if "-dbg" in rdepend and "debug-deps" not in skip:
                error_msg = "%s rdepends on %s" % (pkgname,rdepend)
                sane = package_qa_handle_error("debug-deps", error_msg, d)
            if (not "-dev" in pkg and not "-staticdev" in pkg) and rdepend.endswith("-dev") and "dev-deps" not in skip:
                error_msg = "%s rdepends on %s" % (pkgname, rdepend)
                sane = package_qa_handle_error("dev-deps", error_msg, d)

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

    testmatrix = d.getVarFlags("QAPATHTEST")

    g = globals()
    walk_sane = True
    rdepends_sane = True
    for package in packages.split():
        skip = (bb.data.getVar('INSANE_SKIP_' + package, d, True) or "").split()
        if skip:
            bb.note("Package %s skipping QA tests: %s" % (package, str(skip)))
        warnchecks = []
        for w in (d.getVar("WARN_QA", True) or "").split():
            if w in skip:
               continue
            if w in testmatrix and testmatrix[w] in g:
                warnchecks.append(g[testmatrix[w]])
        errorchecks = []
        for e in (d.getVar("ERROR_QA", True) or "").split():
            if e in skip:
               continue
            if e in testmatrix and testmatrix[e] in g:
                errorchecks.append(g[testmatrix[e]])

        bb.note("Checking Package: %s" % package)
        path = "%s/%s" % (pkgdest, package)
        if not package_qa_walk(path, warnchecks, errorchecks, skip, package, d):
            walk_sane  = False
        if not package_qa_check_rdepends(package, pkgdest, skip, d):
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
       ml = d.getVar("MLPREFIX", True) or ""
       if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('crosssdk', d) or bb.data.inherits_class('nativesdk', d):
          gt = "gettext-native"
       elif bb.data.inherits_class('cross-canadian', d):
          gt = "gettext-nativesdk"
       else:
          gt = "virtual/" + ml + "gettext"
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
