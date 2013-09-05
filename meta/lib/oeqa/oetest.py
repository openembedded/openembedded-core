# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Main unittest module used by testimage.bbclass
# This provides the oeRuntimeTest base class which is inherited by all tests in meta/lib/oeqa/runtime.

# It also has some helper functions and it's responsible for actually starting the tests

import os, re, mmap
import unittest
import inspect
import bb
from oeqa.utils.sshcontrol import SSHControl


def runTests(tc):

    # set the context object passed from the test class
    setattr(oeRuntimeTest, "tc", tc)
    # set ps command to use
    setattr(oeRuntimeTest, "pscmd", "ps -ef" if oeRuntimeTest.hasPackage("procps") else "ps")
    # prepare test suite, loader and runner
    suite = unittest.TestSuite()
    testloader = unittest.TestLoader()
    testloader.sortTestMethodsUsing = None
    runner = unittest.TextTestRunner(verbosity=2)

    bb.note("Test modules  %s" % tc.testslist)
    suite = testloader.loadTestsFromNames(tc.testslist)
    bb.note("Found %s tests" % suite.countTestCases())

    result = runner.run(suite)

    return result



class oeRuntimeTest(unittest.TestCase):

    longMessage = True
    testFailures = []
    testSkipped = []
    testErrors = []

    def __init__(self, methodName='runTest'):
        self.target = oeRuntimeTest.tc.target
        super(oeRuntimeTest, self).__init__(methodName)


    def run(self, result=None):
        super(oeRuntimeTest, self).run(result)

        # we add to our own lists the results, we use those for decorators
        if len(result.failures) > len(oeRuntimeTest.testFailures):
            oeRuntimeTest.testFailures.append(str(result.failures[-1][0]).split()[0])
        if len(result.skipped) > len(oeRuntimeTest.testSkipped):
            oeRuntimeTest.testSkipped.append(str(result.skipped[-1][0]).split()[0])
        if len(result.errors) > len(oeRuntimeTest.testErrors):
            oeRuntimeTest.testErrors.append(str(result.errors[-1][0]).split()[0])

    @classmethod
    def hasPackage(self, pkg):

        pkgfile = os.path.join(oeRuntimeTest.tc.d.getVar("WORKDIR", True), "installed_pkgs.txt")

        with open(pkgfile) as f:
            data = mmap.mmap(f.fileno(), 0, access=mmap.ACCESS_READ)
            match = re.search(pkg, data)
            data.close()

        if match:
            return True

        return False

    @classmethod
    def hasFeature(self,feature):

        if feature in oeRuntimeTest.tc.d.getVar("IMAGE_FEATURES", True).split() or \
                feature in oeRuntimeTest.tc.d.getVar("DISTRO_FEATURES", True).split():
            return True
        else:
            return False

    @classmethod
    def restartTarget(self,params=None):

        if oeRuntimeTest.tc.qemu.restart(params):
            oeRuntimeTest.tc.target.host = oeRuntimeTest.tc.qemu.ip
        else:
            raise Exception("Restarting target failed")


def getmodule(pos=2):
    # stack returns a list of tuples containg frame information
    # First element of the list the is current frame, caller is 1
    frameinfo = inspect.stack()[pos]
    modname = inspect.getmodulename(frameinfo[1])
    #modname = inspect.getmodule(frameinfo[0]).__name__
    return modname

def skipModule(reason, pos=2):
    modname = getmodule(pos)
    if modname not in oeRuntimeTest.tc.testsrequired:
        raise unittest.SkipTest("%s: %s" % (modname, reason))
    else:
        raise Exception("\nTest %s wants to be skipped.\nReason is: %s" \
                "\nTest was required in TEST_SUITES, so either the condition for skipping is wrong" \
                "\nor the image really doesn't have the requred feature/package when it should." % (modname, reason))

def skipModuleIf(cond, reason):

    if cond:
        skipModule(reason, 3)

def skipModuleUnless(cond, reason):

    if not cond:
        skipModule(reason, 3)
