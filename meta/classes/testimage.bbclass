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

# TEST_LOG_DIR contains a ssh log (what command is running, output and return codes) and a qemu boot log till login
# Booting is handled by this class, and it's not a test in itself.
# TEST_QEMUBOOT_TIMEOUT can be used to set the maximum time in seconds the launch code will wait for the login prompt.

TEST_LOG_DIR ?= "${WORKDIR}/testimage"

DEFAULT_TEST_SUITES = "ping auto"
DEFAULT_TEST_SUITES_pn-core-image-minimal = "ping"
DEFAULT_TEST_SUITES_pn-core-image-sato = "ping ssh df connman syslog xorg scp vnc date rpm smart dmesg"
DEFAULT_TEST_SUITES_pn-core-image-sato-sdk = "ping ssh df connman syslog xorg scp vnc date perl ldd gcc rpm smart kernelmodule dmesg"

TEST_SUITES ?= "${DEFAULT_TEST_SUITES}"

TEST_QEMUBOOT_TIMEOUT ?= "1000"

TEST_TARGET ?= "qemu"
TEST_TARGET_IP ?= ""
TEST_SERVER_IP ?= ""

TESTIMAGEDEPENDS = ""
TESTIMAGEDEPENDS_qemuall = "qemu-native:do_populate_sysroot qemu-helper-native:do_populate_sysroot"

python do_testimage() {
    testimage_main(d)
}
addtask testimage
do_testimage[nostamp] = "1"
do_testimage[depends] += "${TESTIMAGEDEPENDS}"


def get_tests_list(d):
    testsuites = d.getVar("TEST_SUITES", True).split()
    bbpath = d.getVar("BBPATH", True).split(':')

    # This relies on lib/ under each directory in BBPATH being added to sys.path
    # (as done by default in base.bbclass)
    testslist = []
    for testname in testsuites:
        if testname != "auto":
            found = False
            for p in bbpath:
                if os.path.exists(os.path.join(p, 'lib', 'oeqa', 'runtime', testname + '.py')):
                    testslist.append("oeqa.runtime." + testname)
                    found = True
                    break
            if not found:
                bb.error('Test %s specified in TEST_SUITES could not be found in lib/oeqa/runtime under BBPATH' % testname)

    if "auto" in testsuites:
        def add_auto_list(path):
            if not os.path.exists(os.path.join(path, '__init__.py')):
                bb.fatal('Tests directory %s exists but is missing __init__.py' % path)
            files = sorted([f for f in os.listdir(path) if f.endswith('.py') and not f.startswith('_')])
            for f in files:
                module = 'oeqa.runtime.' + f[:-3]
                if module not in testslist:
                    testslist.append(module)

        for p in bbpath:
            testpath = os.path.join(p, 'lib', 'oeqa', 'runtime')
            bb.debug(2, 'Searching for tests in %s' % testpath)
            if os.path.exists(testpath):
                add_auto_list(testpath)

    return testslist

def testimage_main(d):
    import unittest
    import os
    import oeqa.runtime
    import time
    from oeqa.oetest import loadTests, runTests
    from oeqa.targetcontrol import get_target_controller

    pn = d.getVar("PN", True)
    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR", True))

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable for a image (like xorg for minimal)
    # testslist is what we'll actually pass to the unittest loader
    testslist = get_tests_list(d)
    testsrequired = [t for t in d.getVar("TEST_SUITES", True).split() if t != "auto"]

    # the robot dance
    target = get_target_controller(d)

    class TestContext:
        def __init__(self):
            self.d = d
            self.testslist = testslist
            self.testsrequired = testsrequired
            self.filesdir = os.path.join(os.path.dirname(os.path.abspath(oeqa.runtime.__file__)),"files")
            self.target = target

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

    try:
        target.start()
        # run tests and get the results
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
