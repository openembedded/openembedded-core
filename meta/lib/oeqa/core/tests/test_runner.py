#!/usr/bin/env python3
#
# Copyright (C) 2016 Intel Corporation
#
# SPDX-License-Identifier: MIT
#

import unittest
import logging
import tempfile

from common import setup_sys_path, TestBase
setup_sys_path()

from oeqa.core.runner import OEStreamLogger

class TestRunner(TestBase):
    def test_stream_logger(self):
        fp = tempfile.TemporaryFile(mode='w+')

        logging.basicConfig(format='%(message)s', stream=fp)
        logger = logging.getLogger()
        logger.setLevel(logging.INFO)

        oeSL = OEStreamLogger(logger)

        lines = ['init', 'bigline_' * 65535, 'morebigline_' * 65535 * 4, 'end']
        for line in lines:
            oeSL.write(line)

        fp.seek(0)
        fp_lines = fp.readlines()
        for i, fp_line in enumerate(fp_lines):
            fp_line = fp_line.strip()
            self.assertEqual(lines[i], fp_line)

        fp.close()

class TestResultStatus(TestBase):
    def _statuses(self, modules):
        tc = self._testLoader(modules=modules)
        result = tc.runTests()
        return {case_id: result._getTestResultDetails(case)[0]
                for case_id, case in tc._registry['cases'].items()}

    def test_result_status(self):
        self.assertEqual(self._statuses(['results', 'resultsmodule']), {
            'results.ResultsTest.testPass': 'PASSED',
            'results.ResultsTest.testFail': 'FAILED',
            'results.ResultsTest.testError': 'ERROR',
            'results.ResultsTest.testSkip': 'SKIPPED',
            'results.ResultsTest.testExpectedFailure': 'EXPECTEDFAIL',
            'results.ResultsTest.testUnexpectedSuccess': 'PASSED',
            'results.ResultsClassErrorTest.testClassError': 'ERROR',
            'results.ResultsClassSkipTest.testClassSkip': 'SKIPPED',
            'resultsmodule.ResultsModuleTest.testModuleError': 'ERROR',
        })

    def test_subtest_status(self):
        self.assertEqual(self._statuses(['subtest']), {
            'subtest.SubTestTest.testSubTestFail': 'FAILED',
            'subtest.SubTestTest.testSubTestPass': 'PASSED',
        })

if __name__ == '__main__':
    unittest.main()
