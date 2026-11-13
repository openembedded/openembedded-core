#!/usr/bin/env python3
#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import os
import unittest
from unittest import mock

from common import setup_sys_path, TestBase
setup_sys_path()

try:
    from oeqa.core.utils.concurrencytest import ConcurrentTestSuite
except ImportError:
    ConcurrentTestSuite = None

@unittest.skipIf(ConcurrentTestSuite is None, "concurrencytest needs testtools, subunit and bitbake")
class TestConcurrency(TestBase):
    def _run(self, modules, processes=None):
        tc = self._testLoader(modules=modules)
        if processes:
            setup = lambda suffix, selftestdir, suite: (None, None)
            tc.prepareSuite = lambda suites, processes: ConcurrentTestSuite(suites, processes, setup, None, {'BBLAYERS': ''})
        # fork_for_tests() only looks up the selftest layer with BUILDDIR set
        with mock.patch.dict(os.environ, {'BUILDDIR': '/nonexistent'}):
            result = tc.runTests(processes=processes)
        return tc, result

    def _statuses(self, modules, processes=None):
        tc, result = self._run(modules, processes)
        return {case_id: result._getTestResultDetails(case)[0]
                for case_id, case in tc._registry['cases'].items()}

    def test_concurrent_status(self):
        # Class and module setup failures must not stop the results of the
        # other tests run by the same process being reported
        modules = ['results', 'resultsmodule', 'subtest']
        sequential = self._statuses(modules)
        self.assertNotIn('UNKNOWN', sequential.values())
        self.assertEqual(self._statuses(modules, processes=1), sequential)

    def test_concurrent_output(self):
        # Each test's results must only have the output from that test, not
        # everything its process has output so far
        tc, result = self._run(['streams'], processes=1)
        for n in (1, 2, 3):
            stdout, stderr = result.logged_output['streams.StreamsTest.testStreams%d' % n]
            output = stdout + stderr
            self.assertIn("Output from testStreams%d" % n, output)
            for other in {1, 2, 3} - {n}:
                self.assertNotIn("testStreams%d" % other, output)

if __name__ == '__main__':
    unittest.main()
