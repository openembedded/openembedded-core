#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase

def setUpModule():
    raise Exception("Error on purpose")

class ResultsModuleTest(OETestCase):

    def testModuleError(self):
        pass
