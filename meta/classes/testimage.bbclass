# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)


# testimage.bbclass enables testing of qemu images using python unittests.
# Most of the tests are commands run on target image over ssh.
# To use it add testimage to global inherit and call your target image with -c testimage
# You can try it out like this:
# - first build a qemu core-image-sato
# - add INHERIT += "testimage" in local.conf
# - then bitbake core-image-sato -c testimage. That will run a standard suite of tests.

# You can set (or append to) TEST_SUITES in local.conf to select the tests
# which you want to run for your target.
# The test names are the module names in meta/lib/oeqa/runtime.
# Each name in TEST_SUITES represents a required test for the image. (no skipping allowed)
# Appending "auto" means that it will try to run all tests that are suitable for the image (each test decides that on it's own).
# Note that order in TEST_SUITES is important (it's the order tests run) and it influences tests dependencies.

# TEST_LOG_DIR contains a ssh log (what command is running, output and return codes) and a qemu boot log till login
# Booting is handled by this class, and it's not a test in itself.
# TEST_QEMUBOOT_TIMEOUT can be used to set the maximum time in seconds the launch code will wait for the login prompt.

TEST_LOG_DIR ?= "${WORKDIR}/testimage"

DEFAULT_TEST_SUITES = "ping auto"
DEFAULT_TEST_SUITES_pn-core-image-minimal = "ping"
DEFAULT_TEST_SUITES_pn-core-image-sato = "ping ssh connman df rpm smart xorg syslog dmesg"
DEFAULT_TEST_SUITES_pn-core-image-sato-sdk = "ping ssh connman df rpm smart gcc xorg syslog dmesg"

TEST_SUITES ?= "${DEFAULT_TEST_SUITES}"

TEST_QEMUBOOT_TIMEOUT ?= "500"

python do_testimage() {
    testimage_main(d)
}
addtask testimage
do_testimage[nostamp] = "1"
do_testimage[depends] += "qemu-native:do_populate_sysroot"
do_testimage[depends] += "qemu-helper-native:do_populate_sysroot"

def testimage_main(d):
    import unittest
    import os
    import oeqa.runtime
    import re
    import shutil
    from oeqa.oetest import runTests
    from oeqa.utils.sshcontrol import SSHControl
    from oeqa.utils.qemurunner import QemuRunner

    testdir = d.getVar("TEST_LOG_DIR", True)
    bb.utils.mkdirhier(testdir)
    sshlog = os.path.join(testdir, "ssh_target_log.%s" % d.getVar('DATETIME', True))
    sshloglink = os.path.join(testdir, "ssh_target_log")
    if os.path.islink(sshloglink):
        os.unlink(sshloglink)
    os.symlink(sshlog, sshloglink)

    # tests in TEST_SUITES become required tests
    # they won't be skipped even if they aren't suitable for a default image (like xorg for minimal)
    testsuites = d.getVar("TEST_SUITES", True)
    # testslist is what we'll run and order matters
    testslist = [ x for x in testsuites.split() if x != "auto" ]
    # if we have auto search for other modules
    if "auto" in testsuites:
        for f in os.listdir(os.path.dirname(os.path.abspath(oeqa.runtime.__file__))):
            if f.endswith('.py') and not f.startswith('_')  and f[:-3] not in testslist:
                testslist.append(f[:-3])

    testslist = [ "oeqa.runtime." + x for x in testslist ]

    class TestContext:
        def __init__(self):
            self.d = d
            self.testslist = testslist
            self.testsrequired = testsuites.split()
            self.filesdir = os.path.join(os.path.dirname(os.path.abspath(oeqa.runtime.__file__)),"files")

    # test context
    tc = TestContext()

    # prepare qemu instance
    # and boot each supported fs type
    machine=d.getVar("MACHINE", True)
    #will handle fs type eventually, stick with ext3 for now
    #make a copy of the original rootfs and use that for tests
    origrootfs=os.path.join(d.getVar("DEPLOY_DIR_IMAGE", True),  d.getVar("IMAGE_LINK_NAME",True) + '.ext3')
    rootfs=os.path.join(testdir, d.getVar("IMAGE_LINK_NAME", True) + '-testimage.ext3')
    try:
        shutil.copyfile(origrootfs, rootfs)
    except Exception as e:
        bb.fatal("Error copying rootfs: %s" % e)

    qemu = QemuRunner(machine, rootfs)
    qemu.tmpdir = d.getVar("TMPDIR", True)
    qemu.display = d.getVar("BB_ORIGENV", False).getVar("DISPLAY", True)
    qemu.logfile = os.path.join(testdir, "qemu_boot_log.%s" % d.getVar('DATETIME', True))
    try:
        qemu.boottime = int(d.getVar("TEST_QEMUBOOT_TIMEOUT", True))
    except ValueError:
        qemu.boottime = 500

    bb.note("DISPLAY value: %s" % qemu.display)
    bb.note("rootfs file: %s" %  rootfs)
    bb.note("Qemu logfile: %s" % qemu.logfile)

    #catch exceptions when loading or running tests (mostly our own errors)
    try:
        if qemu.launch():

            # set more context - ssh instance and qemu
            # we do these here because we needed qemu to boot and get the ip
            tc.qemu = qemu
            tc.target = SSHControl(host=qemu.ip,logfile=sshlog)
            # run tests and get the results
            result = runTests(tc)

            if result.wasSuccessful():
                bb.note("All required tests passed")
            else:
                raise bb.build.FuncFailed("Some tests failed. You should check the task log and the ssh log. (ssh log is %s" % sshlog)

        else:
            raise bb.build.FuncFailed("Failed to start qemu. You should check the task log and the qemu boot log (qemu log is %s)" % qemu.logfile)
    finally:
        qemu.kill()
