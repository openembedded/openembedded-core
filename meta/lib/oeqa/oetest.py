# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Main unittest module used by testimage.bbclass
# This provides the oeRuntimeTest base class which is inherited by all tests in meta/lib/oeqa/runtime.

# It also has some helper functions and it's responsible for actually starting the tests

import os, re, mmap
import unittest
import inspect
import subprocess
from oeqa.utils.decorators import LogResults

def loadTests(tc, type="runtime"):
    if type == "runtime":
        # set the context object passed from the test class
        setattr(oeTest, "tc", tc)
        # set ps command to use
        setattr(oeRuntimeTest, "pscmd", "ps -ef" if oeTest.hasPackage("procps") else "ps")
        # prepare test suite, loader and runner
        suite = unittest.TestSuite()
    elif type == "sdk":
        # set the context object passed from the test class
        setattr(oeTest, "tc", tc)
    testloader = unittest.TestLoader()
    testloader.sortTestMethodsUsing = None
    suite = testloader.loadTestsFromNames(tc.testslist)

    return suite

def runTests(tc, type="runtime"):

    suite = loadTests(tc, type)
    print("Test modules  %s" % tc.testslist)
    print("Found %s tests" % suite.countTestCases())
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)

    return result

@LogResults
class oeTest(unittest.TestCase):

    longMessage = True

    @classmethod
    def hasPackage(self, pkg):

        if re.search(pkg, oeTest.tc.pkgmanifest):
            return True
        return False

    @classmethod
    def hasFeature(self,feature):

        if feature in oeTest.tc.imagefeatures or \
                feature in oeTest.tc.distrofeatures:
            return True
        else:
            return False

class oeRuntimeTest(oeTest):
    def __init__(self, methodName='runTest'):
        self.target = oeRuntimeTest.tc.target
        super(oeRuntimeTest, self).__init__(methodName)

    #TODO: use package_manager.py to install packages on any type of image
    def install_packages(self, packagelist):
        for package in packagelist:
            (status, result) = self.target.run("smart install -y "+package)
            if status != 0:
                return status

class oeSDKTest(oeTest):
    def __init__(self, methodName='runTest'):
        self.sdktestdir = oeSDKTest.tc.sdktestdir
        super(oeSDKTest, self).__init__(methodName)

def getmodule(pos=2):
    # stack returns a list of tuples containg frame information
    # First element of the list the is current frame, caller is 1
    frameinfo = inspect.stack()[pos]
    modname = inspect.getmodulename(frameinfo[1])
    #modname = inspect.getmodule(frameinfo[0]).__name__
    return modname

def skipModule(reason, pos=2):
    modname = getmodule(pos)
    if modname not in oeTest.tc.testsrequired:
        raise unittest.SkipTest("%s: %s" % (modname, reason))
    else:
        raise Exception("\nTest %s wants to be skipped.\nReason is: %s" \
                "\nTest was required in TEST_SUITES, so either the condition for skipping is wrong" \
                "\nor the image really doesn't have the required feature/package when it should." % (modname, reason))

def skipModuleIf(cond, reason):

    if cond:
        skipModule(reason, 3)

def skipModuleUnless(cond, reason):

    if not cond:
        skipModule(reason, 3)
