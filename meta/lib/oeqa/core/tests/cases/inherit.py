#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase
from oeqa.core.decorator.depends import OETestDepends

class InheritBaseTest(OETestCase):

    def testInheritFirst(self):
        self.assertTrue(True, msg='How is this possible?')

    @OETestDepends(['testInheritFirst'])
    def testInheritSecond(self):
        self.assertTrue(True, msg='How is this possible?')

class InheritSubTest(InheritBaseTest):
    pass
