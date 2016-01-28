# Copyright (C) 2013 - 2016 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

TEST_LOG_DIR ?= "${WORKDIR}/testimage"
TESTSDKLOCK = "${TMPDIR}/testsdk.lock"

def testsdk_main(d):
    import unittest
    import os
    import glob
    import oeqa.runtime
    import oeqa.sdk
    import time
    import subprocess
    from oeqa.oetest import loadTests, runTests, get_test_suites, get_tests_list

    pn = d.getVar("PN", True)
    bb.utils.mkdirhier(d.getVar("TEST_LOG_DIR", True))

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable.
    # testslist is what we'll actually pass to the unittest loader
    testslist = get_tests_list(get_test_suites(d, "sdk"), d.getVar("BBPATH", True).split(':'), "sdk")
    testsrequired = [t for t in (d.getVar("TEST_SUITES_SDK", True) or "auto").split() if t != "auto"]

    tcname = d.expand("${SDK_DEPLOY}/${TOOLCHAIN_OUTPUTNAME}.sh")
    if not os.path.exists(tcname):
        bb.fatal("The toolchain is not built. Build it before running the tests: 'bitbake <image> -c populate_sdk' .")

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
            manifest = d.getVar("SDK_TARGET_MANIFEST", True)
            try:
                with open(manifest) as f:
                    self.pkgmanifest = f.read()
            except IOError as e:
                bb.fatal("No package manifest file found. Did you build the sdk image?\n%s" % e)
            hostmanifest = d.getVar("SDK_HOST_MANIFEST", True)
            try:
                with open(hostmanifest) as f:
                    self.hostpkgmanifest = f.read()
            except IOError as e:
                bb.fatal("No host package manifest file found. Did you build the sdk image?\n%s" % e)

    sdktestdir = d.expand("${WORKDIR}/testimage-sdk/")
    bb.utils.remove(sdktestdir, True)
    bb.utils.mkdirhier(sdktestdir)
    try:
        subprocess.check_output("cd %s; %s <<EOF\n./tc\nY\nEOF" % (sdktestdir, tcname), shell=True)
    except subprocess.CalledProcessError as e:
        bb.fatal("Couldn't install the SDK:\n%s" % e.output)

    try:
        targets = glob.glob(d.expand(sdktestdir + "/tc/environment-setup-*"))
        for sdkenv in targets:
            bb.plain("Testing %s" % sdkenv)
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

            starttime = time.time()
            result = runTests(tc, "sdk")
            stoptime = time.time()
            if result.wasSuccessful():
                bb.plain("%s SDK(%s):%s - Ran %d test%s in %.3fs" % (pn, os.path.basename(tcname), os.path.basename(sdkenv),result.testsRun, result.testsRun != 1 and "s" or "", stoptime - starttime))
                msg = "%s - OK - All required tests passed" % pn
                skipped = len(result.skipped)
                if skipped:
                    msg += " (skipped=%d)" % skipped
                bb.plain(msg)
            else:
                raise bb.build.FuncFailed("%s - FAILED - check the task log and the commands log" % pn )
    finally:
        bb.utils.remove(sdktestdir, True)

testsdk_main[vardepsexclude] =+ "BB_ORIGENV"

python do_testsdk() {
    testsdk_main(d)
}
addtask testsdk
do_testsdk[nostamp] = "1"
do_testsdk[lockfiles] += "${TESTSDKLOCK}"

TEST_LOG_SDKEXT_DIR ?= "${WORKDIR}/testsdkext"
TESTSDKEXTLOCK = "${TMPDIR}/testsdkext.lock"

def testsdkext_main(d):
    import unittest
    import os
    import glob
    import oeqa.sdkext
    import time
    import subprocess
    from oeqa.oetest import loadTests, runTests, get_test_suites, get_tests_list
    from bb.utils import export_proxies

    export_proxies(d)

    pn = d.getVar("PN", True)
    bb.utils.mkdirhier(d.getVar("TEST_LOG_SDKEXT_DIR", True))

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable.
    # testslist is what we'll actually pass to the unittest loader
    testslist = get_tests_list(get_test_suites(d, "sdkext"),
                    d.getVar("BBPATH", True).split(':'), "sdkext")
    testsrequired = [t for t in (d.getVar("TEST_SUITES_SDKEXT", True) or \
                    "auto").split() if t != "auto"]

    tcname = d.expand("${SDK_DEPLOY}/${TOOLCHAINEXT_OUTPUTNAME}.sh")
    if not os.path.exists(tcname):
        bb.fatal("The toolchain ext is not built. Build it before running the" \
                 " tests: 'bitbake <image> -c populate_sdk_ext' .")

    testdir = d.expand("${WORKDIR}/testsdkext/")
    bb.utils.remove(testdir, True)
    bb.utils.mkdirhier(testdir)
    try:
        subprocess.check_output("%s -y -d %s" % (tcname, testdir), shell=True)
    except subprocess.CalledProcessError as e:
        bb.fatal("Couldn't install the SDK EXT:\n%s" % e.output)

testsdkext_main[vardepsexclude] =+ "BB_ORIGENV"

python do_testsdkext() {
    testsdkext_main(d)
}
addtask testsdkext
do_testsdkext[nostamp] = "1"
do_testsdkext[lockfiles] += "${TESTSDKEXTLOCK}"
