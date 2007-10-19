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
#  -Check if packages contains .debug directories  or .so files where they should be in -dev or -dbg
#  -Check if config.log contains traces to broken autoconf tests
#


#
# We need to have the scanelf utility as soon as
# possible and this is contained within the pax-utils-native
#


# We play a special package function
inherit package
PACKAGE_DEPENDS += "pax-utils-native"
#PACKAGE_DEPENDS += chrpath-native"
PACKAGEFUNCS += " do_package_qa "


#
# dictionary for elf headers
#
# feel free to add and correct. 
#
#           TARGET_OS  TARGET_ARCH   MACHINE, OSABI, ABIVERSION, Little Endian, 32bit?
def package_qa_get_machine_dict():
    return {
            "linux" : { 
                        "arm" :       (40,    97,    0,          True,          True),
                        "armeb":      (40,    97,    0,          False,         True),
                        "powerpc":    (20,     0,    0,          False,         True),
                        "i386":       ( 3,     0,    0,          True,          True),
                        "i486":       ( 3,     0,    0,          True,          True),
                        "i586":       ( 3,     0,    0,          True,          True),
                        "i686":       ( 3,     0,    0,          True,          True),
                        "x86_64":     (62,     0,    0,          True,          False),
                        "ia64":       (50,     0,    0,          True,          False),
                        "alpha":      (36902,  0,    0,          True,          False),
                        "hppa":       (15,     3,    0,          False,         True),
                        "m68k":       ( 4,     0,    0,          False,         True),
                        "mips":       ( 8,     0,    0,          False,         True),
                        "mipsel":     ( 8,     0,    0,          True,          True),
                        "s390":       (22,     0,    0,          False,         True),
                        "sh4":        (42,     0,    0,          True,          True),
                        "sparc":      ( 2,     0,    0,          False,         True),
                      },
            "linux-uclibc" : { 
                        "arm" :       (  40,    97,    0,          True,          True),
                        "armeb":      (  40,    97,    0,          False,         True),
                        "powerpc":    (  20,     0,    0,          False,         True),
                        "mipsel":     (   8,     0,    0,          True,          True),
			"avr32":      (6317,     0,    0,          False,         True),
                      },
            "uclinux-uclibc" : {
                        "bfin":       ( 106,     0,    0,          True,         True),
                      }, 
            "linux-gnueabi" : {
                        "arm" :       (40,     0,    0,          True,          True),
                        "armeb" :     (40,     0,    0,          False,         True),
                      },
            "linux-uclibcgnueabi" : {
                        "arm" :       (40,     0,    0,          True,          True),
                        "armeb" :     (40,     0,    0,          False,         True),
                      },
 
       }

# factory for a class, embedded in a method
def package_qa_get_elf(path, bits32):
    class ELFFile:
        EI_NIDENT = 16

        EI_CLASS      = 4
        EI_DATA       = 5
        EI_VERSION    = 6
        EI_OSABI      = 7
        EI_ABIVERSION = 8

        # possible values for EI_CLASS
        ELFCLASSNONE = 0
        ELFCLASS32   = 1
        ELFCLASS64   = 2

        # possible value for EI_VERSION
        EV_CURRENT   = 1

        # possible values for EI_DATA
        ELFDATANONE  = 0
        ELFDATA2LSB  = 1
        ELFDATA2MSB  = 2

        def my_assert(expectation, result):
            if not expectation == result:
                #print "'%x','%x'" % (ord(expectation), ord(result))
                raise Exception("This does not work as expected")
        my_assert = staticmethod(my_assert)

        def __init__(self, name):
            self.name = name

        def open(self):
            self.file = file(self.name, "r")
            self.data = self.file.read(ELFFile.EI_NIDENT+4)

            ELFFile.my_assert(len(self.data), ELFFile.EI_NIDENT+4)
            ELFFile.my_assert(self.data[0], chr(0x7f) )
            ELFFile.my_assert(self.data[1], 'E')
            ELFFile.my_assert(self.data[2], 'L')
            ELFFile.my_assert(self.data[3], 'F')
            if bits32 :
                ELFFile.my_assert(self.data[ELFFile.EI_CLASS], chr(ELFFile.ELFCLASS32)) # only 32 bits
            else:
                ELFFile.my_assert(self.data[ELFFile.EI_CLASS], chr(ELFFile.ELFCLASS64)) # only 64 bits
            ELFFile.my_assert(self.data[ELFFile.EI_VERSION], chr(ELFFile.EV_CURRENT) )

            self.sex = self.data[ELFFile.EI_DATA]
            if self.sex == chr(ELFFile.ELFDATANONE):
                raise Exception("self.sex == ELFDATANONE")
            elif self.sex == chr(ELFFile.ELFDATA2LSB):
                self.sex = "<"
            elif self.sex == chr(ELFFile.ELFDATA2MSB):
                self.sex = ">"
            else:
                raise Exception("Unknown self.sex")

        def osAbi(self):
            return ord(self.data[ELFFile.EI_OSABI])

        def abiVersion(self):
            return ord(self.data[ELFFile.EI_ABIVERSION])

        def isLittleEndian(self):
            return self.sex == "<"

        def isBigEngian(self):
            return self.sex == ">"

        def machine(self):
            """
            We know the sex stored in self.sex and we
            know the position
            """
            import struct
            (a,) = struct.unpack(self.sex+"H", self.data[18:20])
            return a

    return ELFFile(path)


#
#
# Known Error classes
# 0 - non dev contains .so
# 1 - package contains a dangerous RPATH
# 2 - package depends on debug package
# 3 - non dbg contains .so
# 4 - wrong architecture
# 5 - .la contains installed=yes or reference to the workdir
# 6 - .pc contains reference to /usr/include or workdir
#
#

def package_qa_clean_path(path,d):
    import bb
    return path.replace(bb.data.getVar('TMPDIR',d,True),"")

def package_qa_make_fatal_error(error_class, name, path,d):
    """
    decide if an error is fatal

    TODO: Load a whitelist of known errors
    """
    if error_class == 0:
        return False
    else:
        return True

def package_qa_write_error(error_class, name, path, d):
    import bb, os
    if not bb.data.getVar('QA_LOG', d):
        return

    ERROR_NAMES =[
        "non dev contains .so",
        "package contains RPATH",
        "package depends on debug package",
        "non dbg contains .debug",
        "wrong architecture",
        "evil hides inside the .la",
        "evil hides inside the .pc",
    ]


    log_path = os.path.join( bb.data.getVar('T', d, True), "log.qa_package" )
    f = file( log_path, "a+")
    print >> f, "%s, %s, %s" % (ERROR_NAMES[error_class], name, package_qa_clean_path(path,d))
    f.close()


def package_qa_check_rpath(file,name,d):
    """
    Check for dangerous RPATHs
    """
    import bb, os
    scanelf = os.path.join(bb.data.getVar('STAGING_BINDIR_NATIVE',d,True),'scanelf')
    #chrpath = os.path.join(bb.data.getVar('STAGING_BINDIR_NATIVE',d,True),'chrpath')
    bad_dir = bb.data.getVar('TMPDIR', d, True) + "/work"
    bad_dir_test = bb.data.getVar('TMPDIR', d, True)
    if not os.path.exists(scanelf):
        bb.fatal("Can not check RPATH, scanelf (part of pax-utils-native) not found")
    #if not os.path.exists(chrpath):
    #    bb.fatal("Can not fix RPATH, chrpath (part of chrpath-native) not found")
    if not bad_dir in bb.data.getVar('WORKDIR', d, True):
        bb.fatal("This class assumed that WORKDIR is ${TMPDIR}/work... Not doing any check")

    #bb.note("%s -B -F%%r#F %s" % (scanelf,file))
    output = os.popen("%s -B -F%%r#F '%s'" % (scanelf,file))
    txt    = output.readline().split()
    #bb.note("???%s???" % bad_dir_test)
    for line in txt:
        #bb.note("===%s===" % line)
        if bad_dir in line:
            package_qa_write_error( 1, name, file, d)
            bb.error("QA Issue package %s contains bad RPATH %s in file %s" % (name, line, file))
            #bb.note("Fixing RPATH for you in %s" % file)
            #os.popen("%s -r /lib %s" % (chrpath,file))
            #return False
    return True

def package_qa_check_devdbg(path, name,d):
    """
    Check for debug remains inside the binary or
    non dev packages containing
    """

    import bb, os
    sane = True

    if not "-dev" in name:
        if path[-3:] == ".so" and os.path.islink(path):
            package_qa_write_error( 0, name, path, d )
            bb.error("QA Issue: non -dev package contains symlink .so: %s path '%s'" % (name, package_qa_clean_path(path,d)))
            if package_qa_make_fatal_error( 0, name, path, d ):
                sane = False

    if not "-dbg" in name:
        if '.debug' in path:
            package_qa_write_error( 3, name, path, d )
            bb.error("QA Issue: non debug package contains .debug directory: %s path %s" % (name, package_qa_clean_path(path,d)))
            if package_qa_make_fatal_error( 3, name, path, d ):
                sane = False

    return sane

def package_qa_check_perm(path,name,d):
    """
    Check the permission of files
    """
    sane = True
    return sane

def package_qa_check_arch(path,name,d):
    """
    Check if archs are compatible
    """
    import bb, os
    target_os   = bb.data.getVar('TARGET_OS',   d, True)
    target_arch = bb.data.getVar('TARGET_ARCH', d, True)
  
    # FIXME: Cross package confuse this check, so just skip them
    if bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d):
        return True

    # avoid following links to /usr/bin (e.g. on udev builds)
    # we will check the files pointed to anyway...
    if os.path.islink(path):
        return True

    #if this will throw an exception, then fix the dict above
    (machine, osabi, abiversion, littleendian, bits32) = package_qa_get_machine_dict()[target_os][target_arch]
    elf = package_qa_get_elf(path, bits32)
    try:
        elf.open()
    except:
        # just for debbugging to check the parser, remove once convinced...
        return True

    if not machine == elf.machine():
        bb.error("Architecture did not match (%d to %d) on %s" %(machine, elf.machine(), package_qa_clean_path(path,d)))
        return not package_qa_make_fatal_error( 4, name, path, d )
    elif not littleendian == elf.isLittleEndian():
        bb.error("Endiannes did not match (%d to %d) on %s" % (littleendian, elf.isLittleEndian(), package_qa_clean_path(path,d)))
        return not package_qa_make_fatal_error( 4, name, path, d )

    return True

def package_qa_check_pcla(path,name,d):
    """
    .pc and .la files should not point to the WORKDIR
    """
    sane = True
    return sane

def package_qa_check_staged(path,d):
    """
    Check staged la and pc files for sanity
      -e.g. installed being false

        As this is run after every stage we should be able
        to find the one responsible for the errors easily even
        if we look at every .pc and .la file
    """
    import os, bb

    sane = True
    workdir = os.path.join(bb.data.getVar('TMPDIR', d, True), "work")

    if bb.data.inherits_class("native", d):
        installed = "installed=no"
    else:
        installed = "installed=yes"

    # find all .la and .pc files
    # read the content
    # and check for stuff that looks wrong
    for root, dirs, files in os.walk(path):
        for file in files:
            path = os.path.join(root,file)
            if file[-2:] == "la":
                file_content = open(path).read()
                if installed in file_content or workdir in file_content:
                    bb.error("QA issue: %s failed sanity test (reference to workdir or installed)" % file )
                    if package_qa_make_fatal_error( 5, "staging", path, d):
                        sane = True
            elif file[-2:] == "pc":
                file_content = open(path).read()
                if workdir in file_content:
                    bb.error("QA issue: %s failed sanity test (reference to workdir)" % file )
                    if package_qa_make_fatal_error( 6, "staging", path, d):
                        sane = False

    return sane

# Walk over all files in a directory and call func
def package_qa_walk(path, funcs, package,d):
    import os
    sane = True

    for root, dirs, files in os.walk(path):
        for file in files:
            path = os.path.join(root,file)
            for func in funcs:
                if not func(path, package,d):
                    sane = False

    return sane


def package_qa_check_rdepends(pkg, workdir, d):
    import bb
    sane = True
    if not "-dbg" in pkg and not "task-" in pkg and not "-image" in pkg:
        # Copied from package_ipk.bbclass
        # boiler plate to update the data
        localdata = bb.data.createCopy(d)
        root = "%s/install/%s" % (workdir, pkg)

        bb.data.setVar('ROOT', '', localdata) 
        bb.data.setVar('ROOT_%s' % pkg, root, localdata)
        pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, True)
        if not pkgname:
            pkgname = pkg
        bb.data.setVar('PKG', pkgname, localdata)

        overrides = bb.data.getVar('OVERRIDES', localdata)
        if not overrides:
            raise bb.build.FuncFailed('OVERRIDES not defined')
        overrides = bb.data.expand(overrides, localdata)
        bb.data.setVar('OVERRIDES', overrides + ':' + pkg, localdata)

        bb.data.update_data(localdata)

        # Now check the RDEPENDS
        rdepends = explode_deps(bb.data.getVar('RDEPENDS', localdata, True) or "")


        # Now do the sanity check!!!
        for rdepend in rdepends:
            if "-dbg" in rdepend:
                package_qa_write_error( 2, pkgname, rdepend, d )
                bb.error("QA issue: %s rdepends on %s" % (pkgname,rdepend))
                if package_qa_make_fatal_error( 2, pkgname, rdepend, d ):
                    sane = False

    return sane

# The PACKAGE FUNC to scan each package
python do_package_qa () {
    bb.note("DO PACKAGE QA")
    workdir = bb.data.getVar('WORKDIR', d, True)
    packages = bb.data.getVar('PACKAGES',d, True)

    # no packages should be scanned
    if not packages:
        return

    walk_sane = True
    rdepends_sane = True
    for package in packages.split():
        if bb.data.getVar('INSANE_SKIP_' + package, d, True):
            bb.note("Package: %s (skipped)" % package)
            continue

        bb.note("Checking Package: %s" % package)
        path = "%s/install/%s" % (workdir, package)
        if not package_qa_walk(path, [package_qa_check_rpath, package_qa_check_devdbg, package_qa_check_perm, package_qa_check_arch], package, d):
            walk_sane  = False
        if not package_qa_check_rdepends(package, workdir, d):
            rdepends_sane = False

    if not walk_sane or not rdepends_sane:
        bb.fatal("QA run found fatal errors. Please consider fixing them.")
    bb.note("DONE with PACKAGE QA")
}


# The Staging Func, to check all staging
addtask qa_staging after do_populate_staging before do_build
python do_qa_staging() {
    bb.note("QA checking staging")

    if not package_qa_check_staged(bb.data.getVar('STAGING_LIBDIR',d,True), d):
        bb.fatal("QA staging was broken by the package built above")
}

# Check broken config.log files
addtask qa_configure after do_configure before do_compile
python do_qa_configure() {
    bb.note("Checking sanity of the config.log file")
    import os
    for root, dirs, files in os.walk(bb.data.getVar('WORKDIR', d, True)):
        if "config.log" in files:
            if os.system("grep 'CROSS COMPILE Badness:' %s > /dev/null" % (os.path.join(root,"config.log"))) == 0:
                bb.fatal("This autoconf log indicates errors, it looked at host includes. Rerun configure task after fixing this. Path was '%s'" % root)
}
