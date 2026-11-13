#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import unittest

from oeqa.core.case import OETestCase

class ResultsTest(OETestCase):

    def testPass(self):
        pass

    def testFail(self):
        self.fail("Failed on purpose")

    def testError(self):
        raise Exception("Error on purpose")

    def testSkip(self):
        self.skipTest("Skipped on purpose")

    @unittest.expectedFailure
    def testExpectedFailure(self):
        self.fail("Failed on purpose")

    @unittest.expectedFailure
    def testUnexpectedSuccess(self):
        pass

class ResultsClassErrorTest(OETestCase):

    @classmethod
    def setUpClass(cls):
        raise Exception("Error on purpose")

    def testClassError(self):
        pass

class ResultsClassSkipTest(OETestCase):

    @classmethod
    def setUpClass(cls):
        raise unittest.SkipTest("Skipped on purpose")

    def testClassSkip(self):
        pass
