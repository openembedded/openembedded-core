#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase

class SubTestTest(OETestCase):

    def testSubTestFail(self):
        for i in range(2):
            with self.subTest(i=i):
                self.assertEqual(i, 0)

    def testSubTestPass(self):
        for i in range(2):
            with self.subTest(i=i):
                self.assertTrue(True, msg='How is this possible?')
