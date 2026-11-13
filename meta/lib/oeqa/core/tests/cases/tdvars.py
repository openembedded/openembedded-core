#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

from oeqa.core.case import OETestCase

class TdVarsMissingTest(OETestCase):
    td_vars = ['NOT_IN_TD']

    def testTdVarsMissing(self):
        pass
