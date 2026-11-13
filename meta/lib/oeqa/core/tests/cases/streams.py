#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#

import sys

from oeqa.core.case import OETestCase

class StreamsTest(OETestCase):

    def testStreams1(self):
        print("Output from testStreams1")
        sys.stderr.write("Error output from testStreams1\n")

    def testStreams2(self):
        print("Output from testStreams2")
        sys.stderr.write("Error output from testStreams2\n")
        self.fail("Failed on purpose")

    def testStreams3(self):
        print("Output from testStreams3")
        self.fail("Failed on purpose")
