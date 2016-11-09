# Copyright (C) 2016 Intel Corporation
# Released under the MIT license (see COPYING.MIT)

import os
import unittest

def getSuiteCases(suite):
    """
        Returns individual test from a test suite.
    """
    tests = []
    for item in suite:
        if isinstance(item, unittest.suite.TestSuite):
            tests.extend(getSuiteCases(item))
        elif isinstance(item, unittest.TestCase):
            tests.append(item)
    return tests

def getSuiteModules(suite):
    """
        Returns modules in a test suite.
    """
    modules = set()
    for test in getSuiteCases(suite):
        modules.add(getCaseModule(test))
    return modules

def getSuiteCasesInfo(suite, func):
    """
        Returns test case info from suite. Info is fetched from func.
    """
    tests = []
    for test in getSuiteCases(suite):
        tests.append(func(test))
    return tests

def getSuiteCasesNames(suite):
    """
        Returns test case names from suite.
    """
    return getSuiteCasesInfo(suite, getCaseMethod)

def getSuiteCasesIDs(suite):
    """
        Returns test case ids from suite.
    """
    return getSuiteCasesInfo(suite, getCaseID)

def getCaseModule(test_case):
    """
        Returns test case module name.
    """
    return test_case.__module__

def getCaseClass(test_case):
    """
        Returns test case class name.
    """
    return test_case.__class__.__name__

def getCaseID(test_case):
    """
        Returns test case complete id.
    """
    return test_case.id()

def getCaseMethod(test_case):
    """
        Returns test case method name.
    """
    return getCaseID(test_case).split('.')[-1]
