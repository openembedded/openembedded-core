# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)


# testimage.bbclass enables testing of qemu images using python unittests.
# Most of the tests are commands run on target image over ssh.
# To use it add testimage to global inherit and call your target image with -c testimage
# You can try it out like this:
# - first build a qemu core-image-sato
# - add INHERIT += "testimage" in local.conf
# - then bitbake core-image-sato -c testimage. That will run a standard suite of tests.

# You can set (or append to) TEST_SUITES in local.conf to select the tests
# which you want to run for your target.
# The test names are the module names in meta/lib/oeqa/runtime.
# Each name in TEST_SUITES represents a required test for the image. (no skipping allowed)
# Appending "auto" means that it will try to run all tests that are suitable for the image (each test decides that on it's own).
# Note that order in TEST_SUITES is important (it's the order tests run) and it influences tests dependencies.
# A layer can add its own tests in lib/oeqa/runtime, provided it extends BBPATH as normal in its layer.conf.

# TEST_LOG_DIR contains a command ssh log and may contain infromation about what command is running, output and return codes and for qemu a boot log till login.
# Booting is handled by this class, and it's not a test in itself.
# TEST_QEMUBOOT_TIMEOUT can be used to set the maximum time in seconds the launch code will wait for the login prompt.

TEST_LOG_DIR ?= "${WORKDIR}/testimage"

TEST_EXPORT_DIR ?= "${TMPDIR}/testimage/${PN}"
TEST_EXPORT_ONLY ?= "0"

DEFAULT_TEST_SUITES = "ping auto"
DEFAULT_TEST_SUITES_pn-core-image-minimal = "ping"
DEFAULT_TEST_SUITES_pn-core-image-sato = "ping ssh df connman syslog xorg scp vnc date rpm smart dmesg python parselogs"
DEFAULT_TEST_SUITES_pn-core-image-sato-sdk = "ping ssh df connman syslog xorg scp vnc date perl ldd gcc rpm smart kernelmodule dmesg python parselogs"
DEFAULT_TEST_SUITES_pn-meta-toolchain = "auto"
TEST_SUITES ?= "${DEFAULT_TEST_SUITES}"

TEST_QEMUBOOT_TIMEOUT ?= "1000"
TEST_TARGET ?= "qemu"
TEST_TARGET_IP ?= ""
TEST_SERVER_IP ?= ""

TESTIMAGEDEPENDS = ""
TESTIMAGEDEPENDS_qemuall = "qemu-native:do_populate_sysroot qemu-helper-native:do_populate_sysroot"

TESTIMAGELOCK = "${TMPDIR}/testimage.lock"
TESTIMAGELOCK_qemuall = ""

python do_testimage() {
    testimage_main(d)
}
addtask testimage
do_testimage[nostamp] = "1"
do_testimage[depends] += "${TESTIMAGEDEPENDS}"
do_testimage[lockfiles] += "${TESTIMAGELOCK}"

python do_testsdk() {
    testsdk_main(d)
}
addtask testsdk
do_testsdk[nostamp] = "1"
do_testsdk[depends] += "${TESTIMAGEDEPENDS}"
do_testsdk[lockfiles] += "${TESTIMAGELOCK}"

def get_tests_list(d, type="runtime"):
    testsuites = d.getVar("TEST_SUITES", True).split()
    bbpath = d.getVar("BBPATH", True).split(':')

    # This relies on lib/ under each directory in BBPATH being added to sys.path
    # (as done by default in base.bbclass)
    testslist = []
    for testname in testsuites:
        if testname != "auto":
            found = False
            for p in bbpath:
                if os.path.exists(os.path.join(p, 'lib', 'oeqa', type, testname + '.py')):
                    testslist.append("oeqa." + type + "." + testname)
                    found = True
                    break
            if not found:
                bb.fatal('Test %s specified in TEST_SUITES could not be found in lib/oeqa/runtime under BBPATH' % testname)

    if "auto" in testsuites:
        def add_auto_list(path):
            if not os.path.exists(os.path.join(path, '__init__.py')):
                bb.fatal('Tests directory %s exists but is missing __init__.py' % path)
            files = sorted([f for f in os.listdir(path) if f.endswith('.py') and not f.startswith('_')])
            for f in files:
                module = 'oeqa.' + type + '.' + f[:-3]
                if module not in testslist:
                    testslist.append(module)

        for p in bbpath:
            testpath = os.path.join(p, 'lib', 'oeqa', type)
            bb.debug(2, 'Searching for tests in %s' % testpath)
            if os.path.exists(testpath):
                add_auto_list(testpath)

    return testslist


def exportTests(d,tc):
    import json
    import shutil
    import pkgutil

    exportpath = d.getVar("TEST_EXPORT_DIR", True)

    savedata = {}
    savedata["d"] = {}
    savedata["target"] = {}
    for key in tc.__dict__:
        # special cases
        if key != "d" and key != "target":
            savedata[key] = getattr(tc, key)
    savedata["target"]["ip"] = tc.target.ip or d.getVar("TEST_TARGET_IP", True)
    savedata["target"]["server_ip"] = tc.target.server_ip or d.getVar("TEST_SERVER_IP", True)

    keys = [ key for key in d.keys() if not key.startswith("_") and not key.startswith("BB") \
            and not key.startswith("B_pn") and not key.startswith("do_") and not d.getVarFlag(key, "func")]
    for key in keys:
        try:
            savedata["d"][key] = d.getVar(key, True)
        except bb.data_smart.ExpansionError:
            # we don't care about those anyway
            pass

    with open(os.path.join(exportpath, "testdata.json"), "w") as f:
            json.dump(savedata, f, skipkeys=True, indent=4, sort_keys=True)

    # now start copying files
    # we'll basically copy everything under meta/lib/oeqa, with these exceptions
    #  - oeqa/targetcontrol.py - not needed
    #  - oeqa/selftest - something else
    # That means:
    #   - all tests from oeqa/runtime defined in TEST_SUITES (including from other layers)
    #   - the contents of oeqa/utils and oeqa/runtime/files
    #   - oeqa/oetest.py and oeqa/runexport.py (this will get copied to exportpath not exportpath/oeqa)
    #   - __init__.py files
    bb.utils.mkdirhier(os.path.join(exportpath, "oeqa/runtime/files"))
    bb.utils.mkdirhier(os.path.join(exportpath, "oeqa/utils"))
    # copy test modules, this should cover tests in other layers too
    for t in tc.testslist:
        mod = pkgutil.get_loader(t)
        shutil.copy2(mod.filename, os.path.join(exportpath, "oeqa/runtime"))
    # copy __init__.py files
    oeqadir = pkgutil.get_loader("oeqa").filename
    shutil.copy2(os.path.join(oeqadir, "__init__.py"), os.path.join(exportpath, "oeqa"))
    shutil.copy2(os.path.join(oeqadir, "runtime/__init__.py"), os.path.join(exportpath, "oeqa/runtime"))
    # copy oeqa/oetest.py and oeqa/runexported.py
    shutil.copy2(os.path.join(oeqadir, "oetest.py"), os.path.join(exportpath, "oeqa"))
    shutil.copy2(os.path.join(oeqadir, "runexported.py"), exportpath)
    # copy oeqa/utils/*.py
    for root, dirs, files in os.walk(os.path.join(oeqadir, "utils")):
        for f in files:
            if f.endswith(".py"):
                shutil.copy2(os.path.join(root, f), os.path.join(exportpath, "oeqa/utils"))
    # copy oeqa/runtime/files/*
    for root, dirs, files in os.walk(os.path.join(oeqadir, "runtime/files")):
        for f in files:
            shutil.copy2(os.path.join(root, f), os.path.join(exportpath, "oeqa/runtime/files"))

    bb.plain("Exported tests to: %s" % exportpath)


def testimage_main(d):
    import unittest
    import os
    import oeqa.runtime
    import time
    from oeqa.oetest import loadTests, runTests
    from oeqa.targetcontrol import get_target_controller

    pn = d.getVar("PN", True)
    export = oe.utils.conditional("TEST_EXPORT_ONLY", "1", True, False, d)
    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR", True))
    if export:
        bb.utils.remove(d.getVar("TEST_EXPORT_DIR", True), recurse=True)
        bb.utils.mkdirhier(d.getVar("TEST_EXPORT_DIR", True))

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable for a image (like xorg for minimal)
    # testslist is what we'll actually pass to the unittest loader
    testslist = get_tests_list(d)
    testsrequired = [t for t in d.getVar("TEST_SUITES", True).split() if t != "auto"]

    # the robot dance
    target = get_target_controller(d)

    class TestContext(object):
        def __init__(self):
            self.d = d
            self.testslist = testslist
            self.testsrequired = testsrequired
            self.filesdir = os.path.join(os.path.dirname(os.path.abspath(oeqa.runtime.__file__)),"files")
            self.target = target
            self.imagefeatures = d.getVar("IMAGE_FEATURES", True).split()
            self.distrofeatures = d.getVar("DISTRO_FEATURES", True).split()
            manifest = os.path.join(d.getVar("DEPLOY_DIR_IMAGE", True), d.getVar("IMAGE_LINK_NAME", True) + ".manifest")
            try:
                with open(manifest) as f:
                    self.pkgmanifest = f.read()
            except IOError as e:
                bb.fatal("No package manifest file found. Did you build the image?\n%s" % e)

    # test context
    tc = TestContext()

    # this is a dummy load of tests
    # we are doing that to find compile errors in the tests themselves
    # before booting the image
    try:
        loadTests(tc)
    except Exception as e:
        import traceback
        bb.fatal("Loading tests failed:\n%s" % traceback.format_exc())

    target.deploy()

    target.start()
    try:
        if export:
            exportTests(d,tc)
        else:
            starttime = time.time()
            result = runTests(tc)
            stoptime = time.time()
            if result.wasSuccessful():
                bb.plain("%s - Ran %d test%s in %.3fs" % (pn, result.testsRun, result.testsRun != 1 and "s" or "", stoptime - starttime))
                msg = "%s - OK - All required tests passed" % pn
                skipped = len(result.skipped)
                if skipped:
                    msg += " (skipped=%d)" % skipped
                bb.plain(msg)
            else:
                raise bb.build.FuncFailed("%s - FAILED - check the task log and the ssh log" % pn )
    finally:
        target.stop()

testimage_main[vardepsexclude] =+ "BB_ORIGENV"


def testsdk_main(d):
    import unittest
    import os
    import glob
    import oeqa.runtime
    import oeqa.sdk
    import time
    import subprocess
    from oeqa.oetest import loadTests, runTests

    pn = d.getVar("PN", True)
    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR", True))

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable.
    # testslist is what we'll actually pass to the unittest loader
    testslist = get_tests_list(d, "sdk")
    testsrequired = [t for t in d.getVar("TEST_SUITES", True).split() if t != "auto"]

    sdktestdir = d.expand("${WORKDIR}/testimage-sdk/")
    bb.utils.remove(sdktestdir, True)
    bb.utils.mkdirhier(sdktestdir)

    tcname = d.expand("${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh")
    if not os.path.exists(tcname):
        bb.fatal("The toolchain is not built. Build it before running the tests: 'bitbake meta-toolchain' .")
    subprocess.call("cd %s; %s <<EOF\n./tc\nY\nEOF" % (sdktestdir, tcname), shell=True)

    targets = glob.glob(d.expand(sdktestdir + "/tc/sysroots/*${TARGET_VENDOR}-linux*"))
    if len(targets) > 1:
        bb.fatal("Error, multiple targets within the SDK found and we don't know which to test? %s" % str(targets))
    sdkenv = sdktestdir + "/tc/environment-setup-" + os.path.basename(targets[0])

    class TestContext(object):
        def __init__(self):
            self.d = d
            self.testslist = testslist
            self.testsrequired = testsrequired
            self.filesdir = os.path.join(os.path.dirname(os.path.abspath(oeqa.runtime.__file__)),"files")
            self.sdktestdir = sdktestdir
            self.sdkenv = sdkenv
            self.imagefeatures = d.getVar("IMAGE_FEATURES", True).split()
            self.distrofeatures = d.getVar("DISTRO_FEATURES", True).split()
            manifest = os.path.join(d.getVar("SDK_MANIFEST", True))
            try:
                with open(manifest) as f:
                    self.pkgmanifest = f.read()
            except IOError as e:
                bb.fatal("No package manifest file found. Did you build the sdk image?\n%s" % e)

    # test context
    tc = TestContext()

    # this is a dummy load of tests
    # we are doing that to find compile errors in the tests themselves
    # before booting the image
    try:
        loadTests(tc, "sdk")
    except Exception as e:
        import traceback
        bb.fatal("Loading tests failed:\n%s" % traceback.format_exc())

    try:
        starttime = time.time()
        result = runTests(tc, "sdk")
        stoptime = time.time()
        if result.wasSuccessful():
            bb.plain("%s - Ran %d test%s in %.3fs" % (pn, result.testsRun, result.testsRun != 1 and "s" or "", stoptime - starttime))
            msg = "%s - OK - All required tests passed" % pn
            skipped = len(result.skipped)
            if skipped:
                msg += " (skipped=%d)" % skipped
            bb.plain(msg)
        else:
            raise bb.build.FuncFailed("%s - FAILED - check the task log and the commands log" % pn )
    finally:
        pass
        bb.utils.remove(sdktestdir, True)

testsdk_main[vardepsexclude] =+ "BB_ORIGENV"

