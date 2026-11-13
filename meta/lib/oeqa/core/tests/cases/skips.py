#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase

class SkipsClassTest(OETestCase):

    @classmethod
    def setUpClass(cls):
        raise Exception("setUpClass of a skipped class shouldn't run")

    def testSkipsClass(self):
        pass

class SkipsTest(OETestCase):

    def testSkipsSkipped(self):
        self.fail("Skipped test shouldn't run")

    def testSkipsRun(self):
        pass
