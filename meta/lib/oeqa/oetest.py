# Copyright (C) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# Main unittest module used by testimage.bbclass
# This provides the oeRuntimeTest base class which is inherited by all tests in meta/lib/oeqa/runtime.

# It also has some helper functions and it's responsible for actually starting the tests

import os, re, mmap
import unittest
import inspect


def loadTests(tc):

    # set the context object passed from the test class
    setattr(oeTest, "tc", tc)
    # set ps command to use
    setattr(oeRuntimeTest, "pscmd", "ps -ef" if oeTest.hasPackage("procps") else "ps")
    # prepare test suite, loader and runner
    suite = unittest.TestSuite()
    testloader = unittest.TestLoader()
    testloader.sortTestMethodsUsing = None
    suite = testloader.loadTestsFromNames(tc.testslist)

    return suite

def runTests(tc):

    suite = loadTests(tc)
    print("Test modules  %s" % tc.testslist)
    print("Found %s tests" % suite.countTestCases())
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(suite)

    return result


class oeTest(unittest.TestCase):

    longMessage = True
    testFailures = []
    testSkipped = []
    testErrors = []

    def run(self, result=None):
        super(oeTest, self).run(result)

        # we add to our own lists the results, we use those for decorators
        if len(result.failures) > len(oeTest.testFailures):
            oeTest.testFailures.append(str(result.failures[-1][0]).split()[0])
        if len(result.skipped) > len(oeTest.testSkipped):
            oeTest.testSkipped.append(str(result.skipped[-1][0]).split()[0])
        if len(result.errors) > len(oeTest.testErrors):
            oeTest.testErrors.append(str(result.errors[-1][0]).split()[0])

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
