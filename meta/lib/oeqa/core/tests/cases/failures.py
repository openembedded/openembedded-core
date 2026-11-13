#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase

class FailuresATest(OETestCase):

    def testFailuresA1(self):
        self.fail("Failed on purpose")

    def testFailuresA2(self):
        pass

    def testFailuresA3(self):
        raise Exception("Error on purpose")

class FailuresBTest(OETestCase):

    def testFailuresB1(self):
        pass

    def testFailuresB2(self):
        self.fail("Failed on purpose")

    def testFailuresB3(self):
        self.fail("Failed on purpose")
