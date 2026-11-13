#!/usr/bin/env python3
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: MIT
#

import os
import subprocess
import sys
import textwrap
import unittest

from common import setup_sys_path, TestBase
setup_sys_path()

from oeqa.core.exception import OEQADependency
from oeqa.core.utils.test import getSuiteModules, getSuiteCasesIDs

class TestLoader(TestBase):
    @unittest.skip("invalid directory is missing oetag.py")
    def test_fail_duplicated_module(self):
        cases_path = self.cases_path
        invalid_path = os.path.join(cases_path, 'loader', 'invalid')
        self.cases_path = [self.cases_path, invalid_path]
        expect = 'Duplicated oetag module found in'
        msg = 'Expected ImportError exception for having duplicated module'
        try:
            # Must throw ImportEror because duplicated module
            tc = self._testLoader()
            self.fail(msg)
        except ImportError as e:
            result = True if expect in str(e) else False
            self.assertTrue(result, msg=msg)
        finally:
            self.cases_path = cases_path

    def test_filter_modules(self):
        expected_modules = {'oetag'}
        tc = self._testLoader(modules=expected_modules)
        modules = getSuiteModules(tc.suites)
        msg = 'Expected just %s modules' % ', '.join(expected_modules)
        self.assertEqual(modules, expected_modules, msg=msg)

    def test_filter_cases(self):
        modules = ['oetag', 'data']
        expected_cases = {'data.DataTest.testDataOk',
                          'oetag.TagTest.testTagGood'}
        tc = self._testLoader(modules=modules, tests=expected_cases)
        cases = set(getSuiteCasesIDs(tc.suites))
        msg = 'Expected just %s cases' % ', '.join(expected_cases)
        self.assertEqual(cases, expected_cases, msg=msg)

    def test_import_from_paths(self):
        cases_path = self.cases_path
        cases2_path = os.path.join(cases_path, 'loader', 'valid')
        expected_modules = {'another'}
        self.cases_path = [self.cases_path, cases2_path]
        tc = self._testLoader(modules=expected_modules)
        modules = getSuiteModules(tc.suites)
        self.cases_path = cases_path
        msg = 'Expected modules from two different paths'
        self.assertEqual(modules, expected_modules, msg=msg)

    def test_flat_suite(self):
        # The loaded suite must be a flat list of tests, which skipTests()
        # relies on, even if no module registers a discovery decorator such as
        # OETestDepends. Registering one is global so use a new process.
        oeqa_lib = os.path.realpath(os.path.join(os.path.dirname(os.path.abspath(__file__)), '../../../'))
        cases_path = os.path.join(self.cases_path, 'loader', 'valid')
        script = textwrap.dedent("""
            import logging, sys, unittest
            sys.path.insert(0, %r)
            from oeqa.core.context import OETestContext
            from oeqa.core.decorator import decoratorClasses
            tc = OETestContext({}, logging.getLogger('flat'))
            tc.loadTests(%r)
            assert not [c for c in decoratorClasses if c.__name__ == 'OETestDepends']
            tc.skipTests(['another.AnotherTest.testAnother'])
            print(' '.join(type(t).__name__ for t in tc.suites))
        """) % (oeqa_lib, cases_path)
        output = subprocess.check_output([sys.executable, '-c', script], text=True)
        self.assertEqual(output.split(), ['AnotherTest'])

if __name__ == '__main__':
    unittest.main()
