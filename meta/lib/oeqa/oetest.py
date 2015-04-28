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
import bb
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
    suites = [testloader.loadTestsFromName(name) for name in tc.testslist]

    def getTests(test):
        '''Return all individual tests executed when running the suite.'''
        # Unfortunately unittest does not have an API for this, so we have
        # to rely on implementation details. This only needs to work
        # for TestSuite containing TestCase.
        method = getattr(test, '_testMethodName', None)
        if method:
            # leaf case: a TestCase
            yield test
        else:
            # Look into TestSuite.
            tests = getattr(test, '_tests', [])
            for t1 in tests:
                for t2 in getTests(t1):
                    yield t2

    # Determine dependencies between suites by looking for @skipUnlessPassed
    # method annotations. Suite A depends on suite B if any method in A
    # depends on a method on B.
    for suite in suites:
        suite.dependencies = []
        suite.depth = 0
        for test in getTests(suite):
            methodname = getattr(test, '_testMethodName', None)
            if methodname:
                method = getattr(test, methodname)
                depends_on = getattr(method, '_depends_on', None)
                if depends_on:
                    for dep_suite in suites:
                        if depends_on in [getattr(t, '_testMethodName', None) for t in getTests(dep_suite)]:
                            if dep_suite not in suite.dependencies and \
                               dep_suite is not suite:
                                suite.dependencies.append(dep_suite)
                            break
                    else:
                        bb.warn("Test %s was declared as @skipUnlessPassed('%s') but that test is either not defined or not active. Will run the test anyway." %
                                (test, depends_on))
    # Use brute-force topological sort to determine ordering. Sort by
    # depth (higher depth = must run later), with original ordering to
    # break ties.
    def set_suite_depth(suite):
        for dep in suite.dependencies:
            new_depth = set_suite_depth(dep) + 1
            if new_depth > suite.depth:
                suite.depth = new_depth
        return suite.depth
    for index, suite in enumerate(suites):
        set_suite_depth(suite)
        suite.index = index
    suites.sort(cmp=lambda a,b: cmp((a.depth, a.index), (b.depth, b.index)))
    return testloader.suiteClass(suites)

def runTests(tc, type="runtime"):

    suite = loadTests(tc, type)
    bb.note("Test modules  %s" % tc.testslist)
    bb.note("Found %s tests" % suite.countTestCases())
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

    @classmethod
    def hasHostPackage(self, pkg):

        if re.search(pkg, oeTest.tc.hostpkgmanifest):
            return True
        return False

    def _run(self, cmd):
        return subprocess.check_output(cmd, shell=True)

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
